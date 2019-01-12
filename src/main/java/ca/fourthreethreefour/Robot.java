/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import ca.fourthreethreefour.commands.debug.Logging;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.RobotDrive;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

// If you rename or move this class, update the build.properties file in the project root
public class Robot extends TimedRobot implements Constants
{
    // Initialize an Xbox 360 controller to control the robot
    private XboxController controller;

    // Initialize the drivetrain motors
    private WPI_TalonSRX gearMotor;
    private WPI_TalonSRX leftDriveMotor;
    //private WPI_TalonSRX rightDriveMotor1;
    private WPI_TalonSRX rightDriveMotor;

    // Pairs up the drivetrain motors based on their respective side and initializes the drivetrain controlling object
    private SpeedControllerGroup leftSideDriveMotors;
    private SpeedControllerGroup rightSideDriveMotors;
    private DifferentialDrive robotDrive;

    private AnalogInput lineTracker1;
    private AnalogInput lineTracker2;
    private AnalogInput lineTracker3;

    //Vision Proccessing
    private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 240;
	
	private VisionThread visionThread;
	private double centerX = 0.0;
	private RobotDrive drive;
	
	private final Object imgLock = new Object();

    // Ultrasonic goes here

    
    ShuffleboardTab dynamicSettingsTab = Shuffleboard.getTab("Dynamic Settings");
    ShuffleboardTab portsTab = Shuffleboard.getTab("Ports");
    ShuffleboardTab outputTab = Shuffleboard.getTab("Output");
    
	    NetworkTableEntry LOGGING_ENABLED_ENTRY = dynamicSettingsTab.addPersistent("Logging", false).withWidget(BuiltInWidgets.kToggleSwitch).getEntry();
            static public boolean LOGGING_ENABLED;

        NetworkTableEntry DRIVE_SPEED_ENTRY = dynamicSettingsTab.addPersistent("Drive Speed", 1).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", 1)).getEntry();
            double DRIVE_SPEED;
        NetworkTableEntry DRIVE_COMPENSATION_ENTRY = dynamicSettingsTab.addPersistent("Drive Compensation", 0).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", -0.7, "max", 0.7)).getEntry();
            double DRIVE_COMPENSATION;
        NetworkTableEntry TURN_CURVE_ENTRY = dynamicSettingsTab.addPersistent("Turn Curve", 1.5).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 1, "max", 10)).getEntry();
            double TURN_CURVE;

            

	    NetworkTableEntry XBOXCONTROLLER_ENTRY = portsTab.addPersistent("XboxController", 0).getEntry();
            int XBOXCONTROLLER = (int) XBOXCONTROLLER_ENTRY.getDouble(0);
            
	    NetworkTableEntry GEAR_MOTOR_ENTRY = portsTab.addPersistent("Gear Motor", 2).getEntry();
            int GEAR_MOTOR = (int) GEAR_MOTOR_ENTRY.getDouble(2);
        NetworkTableEntry LEFT_DRIVE_MOTOR_ENTRY = portsTab.addPersistent("Left Drive Motor", 1).getEntry();
            int LEFT_DRIVE_MOTOR = (int) LEFT_DRIVE_MOTOR_ENTRY.getDouble(1);
        NetworkTableEntry RIGHT_DRIVE_MOTOR_ENTRY = portsTab.addPersistent("Right Drive Motor", 3).getEntry();
            int RIGHT_DRIVE_MOTOR = (int) RIGHT_DRIVE_MOTOR_ENTRY.getDouble(3);


        NetworkTableEntry LINE_TRACKER_ENTRY_1 = outputTab.add("Left Line Tracker", 0).getEntry();
        NetworkTableEntry LINE_TRACKER_ENTRY_2 = outputTab.add("Middle Line Tracker", 0).getEntry();
        NetworkTableEntry LINE_TRACKER_ENTRY_3 = outputTab.add("Right Line Tracker", 0).getEntry();

       

    @Override
    public void robotInit()
    {
        // Assigns all the motors to their respective objects (the number in brackets is the port # of what is connected where)
        controller = new XboxController(XBOXCONTROLLER);
        
        gearMotor = new WPI_TalonSRX(GEAR_MOTOR);
        leftDriveMotor = new WPI_TalonSRX(LEFT_DRIVE_MOTOR);
        //rightDriveMotor1 = new WPI_TalonSRX(2);
        rightDriveMotor = new WPI_TalonSRX(RIGHT_DRIVE_MOTOR);

        // Assigns the drivetrain motors to their respective motor controller group and then passes them on to the drivetrain controller object
        leftSideDriveMotors = new SpeedControllerGroup(leftDriveMotor);
        rightSideDriveMotors = new SpeedControllerGroup(rightDriveMotor);
        robotDrive = new DifferentialDrive(leftSideDriveMotors, rightSideDriveMotors);

        lineTracker1 = new AnalogInput(0);
        lineTracker2 = new AnalogInput(1);
        lineTracker3 = new AnalogInput(2);

        // Initialize Camera
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(IMG_WIDTH, IMG_HEIGHT);

        visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
            if (!pipeline.filterContoursOutput().isEmpty()) {
                try {
                    Rect r1 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
                    Rect r2 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(1));
                    System.out.println("Object 1: " + r1.toString());
                    System.out.println("Object 2: " + r2.toString());
                    synchronized (imgLock) {
                        centerX = r1.x + (r1.width / 2);
                    }
                } catch (IndexOutOfBoundsException | NullPointerException e) {
                    System.out.println("No vision target detected " + e.getMessage());
                }
            }
        });
        visionThread.start();
            
        //drive = new RobotDrive(1, 2);

        // Sets the appropriate configuration settings for the motors


        leftSideDriveMotors.setInverted(true);

        rightSideDriveMotors.setInverted(true);
        robotDrive.setSafetyEnabled(true);
        robotDrive.setExpiration(0.1);
        robotDrive.setMaxOutput(0.80);
        gearMotor.setSafetyEnabled(true);
    }

    @Override
    public void autonomousInit()
    {
        // Enables motor safety for the drivetrain for teleop
        robotDrive.setSafetyEnabled(true);
        //gearMotor.setSafetyEnabled(true);
    }

    @Override
    public void autonomousPeriodic()
    {
    }

    @Override
    public void teleopPeriodic()
    {
        double speed = controller.getY(GenericHID.Hand.kLeft);
        double turn = controller.getX(GenericHID.Hand.kRight);
        turn += (speed > 0) ? DRIVE_COMPENSATION : (speed < 0) ? -DRIVE_COMPENSATION : 0;
        // Sends the Y axis input from the left stick (speed) and the X axis input from the right stick (rotation) from the primary controller to move the robot
        robotDrive.arcadeDrive(speed * DRIVE_SPEED, turn >= 0 ? Math.pow(turn, TURN_CURVE) : -Math.pow(Math.abs(turn), TURN_CURVE));
        gearMotor.set(-controller.getTriggerAxis(GenericHID.Hand.kRight));
        /* Logging.log("Speed: " + speed);
        Logging.put(LINE_TRACKER_ENTRY_1, lineTracker1.getValue());
        Logging.put(LINE_TRACKER_ENTRY_2, lineTracker2.getValue());
        Logging.put(LINE_TRACKER_ENTRY_3, lineTracker3.getValue());*/

        // Take .getValue() and based on if the value is greater than x or less than x
        // assign it a boolean with true being on the line, and false being off of it
        
        // If boolean is false, check 
    }

    @Override
    public void disabledPeriodic() {
        updateSettings();
    }

    @Override
    public void testPeriodic(){

    }

    public void updateSettings() {
        LOGGING_ENABLED = LOGGING_ENABLED_ENTRY.getBoolean(false);

        DRIVE_SPEED = DRIVE_SPEED_ENTRY.getDouble(1);
        DRIVE_COMPENSATION = DRIVE_COMPENSATION_ENTRY.getDouble(0);
        TURN_CURVE = TURN_CURVE_ENTRY.getDouble(1.5);
    }
}
