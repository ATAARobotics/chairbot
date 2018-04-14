package main.java.ca.fourthreethreefour;

import java.io.File;
import java.io.IOException;

import edu.first.command.Command;
import edu.first.command.Commands;
import edu.first.module.Module;
import edu.first.module.joysticks.BindingJoystick.DualAxisBind;
import edu.first.module.joysticks.XboxController;
import edu.first.module.subsystems.Subsystem;
import edu.first.robot.IterativeRobotAdapter;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import main.java.ca.fourthreethreefour.commands.ReverseSolenoid;
import main.java.ca.fourthreethreefour.settings.AutoFile;


public class Robot extends IterativeRobotAdapter implements Constants {

	/*
	 * Creates Subsystems AUTO and TELEOP to separate modules required to be enabled
	 * in autonomous and modules required to be enabled in teleoperated mode, 
	 * then puts the two subsystems into ALL_MODULES subsystem. Subsystemception!
	 */
	private final Subsystem 
		AUTO_MODULES = new Subsystem(new Module[] { drive }),
		TELEOP_MODULES = new Subsystem(new Module[] { drive, controllers, speedcontrollers }),
		ALL_MODULES = new Subsystem(new Module[] { AUTO_MODULES, TELEOP_MODULES });

	/*
	 * The current instance of the driver station. Needed in order to send and
	 * receive information (not controller inputs) from the driver station.
	 */
	DriverStation ds = DriverStation.getInstance();

	/*
	 * Constructor for the custom Robot class. Needed because IterativeRobotAdapter
	 * requires a string for some reason. TODO Name the robot!
	 */
	public Robot() {
		super("CHAIR_BOT");
	}

	String settingsActive = settingsFile.toString();
    private Command commandAuto;
	// runs when the robot is first turned on
	@Override
	public void init() {
		// Initializes all modules
		ALL_MODULES.init();

		// Initializes the CameraServer twice. That's how it's done
        CameraServer.getInstance().startAutomaticCapture();
        
		// Controller 1/driver
		/*
		 * Sets the deadband for LEFT_FROM_MIDDLE and RIGHT_X. If the input value from
		 * either of those axes does not exceed the deadband, the value will be set to
		 * zero.
		 */
		controller.addDeadband(XboxController.LEFT_FROM_MIDDLE, 0.12);
		controller.changeAxis(XboxController.LEFT_FROM_MIDDLE, speedFunction);
		controller.addDeadband(XboxController.RIGHT_X, 0.12);
		controller.invertAxis(XboxController.RIGHT_X);
		controller.changeAxis(XboxController.RIGHT_X, turnFunction);
		controller.addDeadband(XboxController.TRIGGERS, 0.15);

		// Creates an axis bind for the left and right sticks
		controller.addAxisBind(new DualAxisBind(controller.getLeftDistanceFromMiddle(), controller.getRightX()) {
			@Override
			public void doBind(double speed, double turn) {
				turn += (speed > 0) ? DRIVE_COMPENSATION : (speed < 0) ? -DRIVE_COMPENSATION : 0;
				drivetrain.arcadeDrive(speed, turn);
				if(Math.abs(speed) < LOW_GEAR_THRESHOLD) {
					gearShifter.set(LOW_GEAR);
				}
			}
		});

		// When right stick is pressed, reverses gearShifter, changing the gear.
		controller.addWhenPressed(XboxController.RIGHT_STICK, new ReverseSolenoid(gearShifter));
		
		controller.addWhilePressed(XboxController.A, new Runnable() {
			@Override
			public void run() {
				speedController1.set(0.5);
				speedController2.set(0.5);
				speedController3.set(0.5);
				speedController4.set(0.5);
			}
		});
		
		controller.addWhilePressed(XboxController.B, new Runnable() {
			@Override
			public void run() {
				speedController1.set(-0.5);
				speedController2.set(-0.5);
				speedController3.set(-0.5);
				speedController4.set(-0.5);
			}
		});
		
	}

	@Override
	public void initDisabled() {
		ALL_MODULES.disable();
	}

	@Override
	public void periodicDisabled() {
		try {
			settingsFile.reload();
		} catch (NullPointerException e) {
			Timer.delay(0.25);
		}

		// TODO add limit switch button to set ARM_PID_TOP constant to current potentiometer value
		
		if (!settingsActive.equalsIgnoreCase(settingsFile.toString())) {
			throw new RuntimeException(); // If it HAS changed, best to crash the Robot so it gets the update.
		}

			try {
				commandAuto = new AutoFile(new File("auto.txt")).toCommand();
			} catch (IOException e) {
				throw new Error(e.getMessage());
			}

		Timer.delay(0.25);
	}

	// Runs at the beginning of autonomous
	@Override
	public void initAutonomous() {
		AUTO_MODULES.enable();
		gearShifter.set(LOW_GEAR);
		drivetrain.setSafetyEnabled(false); // WE DON'T NEED SAFETY
		Commands.run(commandAuto);
	}

	@Override
	public void periodicAutonomous() {
	}
	
	// Runs at the end of autonomous
	@Override
	public void endAutonomous() {
		AUTO_MODULES.disable();
	}

	// Runs at the beginning of teleop
	@Override
	public void initTeleoperated() {
		TELEOP_MODULES.enable();
		drivetrain.setSafetyEnabled(true); // Maybe we do...
		gearShifter.set(LOW_GEAR);
	}

	// Runs every (approx.) 20ms in teleop
	@Override
	public void periodicTeleoperated() {
		// Performs the binds set in init()
		controller.doBinds();
    }

	// Runs at the end of teleop
	@Override
	public void endTeleoperated() {
		TELEOP_MODULES.disable();
	}

}
