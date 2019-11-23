package ca.fourthreethreefour.teleop.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import ca.fourthreethreefour.commands.debug.Logging;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drive extends Subsystem {

    private CANSparkMax frontLeftMotor = new CANSparkMax(3, MotorType.kBrushless);
    private CANSparkMax rearLeftMotor = new CANSparkMax(4, MotorType.kBrushless);
    private CANSparkMax frontRightMotor = new CANSparkMax(1, MotorType.kBrushless);
    private CANSparkMax rearRightMotor = new CANSparkMax(2, MotorType.kBrushless);

    // Initialize the drivetrain motors
    // private WPI_TalonSRX leftDriveMotor = new WPI_TalonSRX(0);
    // private WPI_TalonSRX rightDriveMotor = new WPI_TalonSRX(1);

    // Pairs up the drivetrain motors based on their respective side and initializes the drivetrain controlling object
    private SpeedControllerGroup leftSideDriveMotors = new SpeedControllerGroup(frontLeftMotor, rearLeftMotor);
    private SpeedControllerGroup rightSideDriveMotors = new SpeedControllerGroup(frontRightMotor, rearRightMotor);
    private DifferentialDrive robotDrive = new DifferentialDrive(leftSideDriveMotors, rightSideDriveMotors);

    // Sets the appropriate configuration settings for the motors

    @Override
    protected void initDefaultCommand() {
    }
    public void driveInit() {
        
        leftSideDriveMotors.setInverted(true);
        rightSideDriveMotors.setInverted(true);
        robotDrive.setSafetyEnabled(true);
        robotDrive.setExpiration(0.1);
        robotDrive.setMaxOutput(0.80);

    }

    public void drive(GenericHID controller) {

        double speed = controller.getY(GenericHID.Hand.kLeft);
        double turn = -controller.getX(GenericHID.Hand.kRight);
        turn += (speed > 0) ? 0 : (speed < 0) ? -0 : 0;
        // Sends the Y axis input from the left stick (speed) and the X axis input from the right stick (rotation) from the primary controller to move the robot
        robotDrive.arcadeDrive(speed * 1, turn >= 0 ? Math.pow(turn, 1) : -Math.pow(Math.abs(turn), 1));
        // gearMotor.set(-controller.getTriggerAxis(GenericHID.Hand.kRight));
        Logging.log("Speed: " + speed);
    }

}