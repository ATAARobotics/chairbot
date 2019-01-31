/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour;

import java.awt.Image;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TimedRobot;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.PathfinderFRC;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

// If you rename or move this class, update the build.properties file in the project root
public class Robot extends TimedRobot implements Constants
{
    
    // Initialize the drivetrain motors
    private WPI_TalonSRX gearMotor;
    private WPI_TalonSRX leftDriveMotor;
    // private WPI_TalonSRX rightDriveMotor1;
    private WPI_TalonSRX rightDriveMotor;
    
    // Pairs up the drivetrain motors based on their respective side and initializes
    // the drivetrain controlling object
    private SpeedControllerGroup leftSideDriveMotors;
    private SpeedControllerGroup rightSideDriveMotors;
    private DifferentialDrive robotDrive;
    

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
    
    private static final int k_ticks_per_rev = 1024;
    private static final double k_wheel_diameter = 7;
    private static final double k_max_velocity = 2;
    private static final int k_left_channel = 0;
    private static final int k_right_channel = 1;
    private static final int k_left_encoder_port_a = 0;
    private static final int k_left_encoder_port_b = 1;
    private static final int k_right_encoder_port_a = 2;
    private static final int k_right_encoder_port_b = 3;
    private static final String pathName = "Path1";
    private Encoder leftEncoder;
    private Encoder rightEncoder;
    private AnalogGyro m_gyro;

    private EncoderFollower leftFollower;
    private EncoderFollower rightFollower;
    private Notifier m_follower_notifier;

    
    @Override
    public void robotInit()
    {
        
        gearMotor = new WPI_TalonSRX(GEAR_MOTOR);
        leftDriveMotor = new WPI_TalonSRX(LEFT_DRIVE_MOTOR);
        //rightDriveMotor1 = new WPI_TalonSRX(2);
        rightDriveMotor = new WPI_TalonSRX(RIGHT_DRIVE_MOTOR);
        
        // Assigns the drivetrain motors to their respective motor controller group and then passes them on to the drivetrain controller object
        leftSideDriveMotors = new SpeedControllerGroup(leftDriveMotor);
        rightSideDriveMotors = new SpeedControllerGroup(rightDriveMotor);
        robotDrive = new DifferentialDrive(leftSideDriveMotors, rightSideDriveMotors);
        leftEncoder = new Encoder(k_left_encoder_port_a, k_left_encoder_port_b);
        rightEncoder = new Encoder(k_right_encoder_port_a, k_right_encoder_port_b);



        //drive = new RobotDrive(1, 2);
        
        // Sets the appropriate configuration settings for the motors
        leftSideDriveMotors.setInverted(true);  
        rightSideDriveMotors.setInverted(true);
        robotDrive.setSafetyEnabled(true);
        //robotDrive.setExpiration(0.1);
        robotDrive.setMaxOutput(0.80);
        gearMotor.setSafetyEnabled(true);
        
    }
    
    @Override
    public void autonomousInit()
    {
        robotDrive.setSafetyEnabled(true);
        //Left and Right defineitions are switched due to a known bug.
        //TODO: Add trajectory files in the appropriate directory
        Trajectory leftTrajectory = PathfinderFRC.getTrajectory(pathName + ".right");
        //Creates a trajectory object called "left_trajectory," from a file pathname.right
        Trajectory rightTrajectory = PathfinderFRC.getTrajectory(pathName + ".left");
        //Creates a trajectory object called "rightTrajectory," from a file pathname.left

        //Creates two EncoderFollower objects corrosponding to their respective trajectories
        leftFollower = new EncoderFollower(leftTrajectory);
        rightFollower = new EncoderFollower(rightTrajectory);
    
        //Configures the encoder for the left side for use with EncoderFollower
        leftFollower.configureEncoder(leftEncoder.get(), k_ticks_per_rev, k_wheel_diameter);
        leftFollower.configurePIDVA(1.0, 0.0, 0.0, 1 / k_max_velocity, 0);
    
        //Configures the encoder for the right side for use with EncoderFollower
        rightFollower.configureEncoder(rightEncoder.get(), k_ticks_per_rev, k_wheel_diameter);
        rightFollower.configurePIDVA(1.0, 0.0, 0.0, 1 / k_max_velocity, 0);
        
        
        m_follower_notifier = new Notifier(this::followPath);
        m_follower_notifier.startPeriodic(leftTrajectory.get(0).dt);
        m_follower_notifier.startPeriodic(rightTrajectory.get(0).dt);

      
        // Enables motor safety for the drivetrain for teleop
        
        //gearMotor.setSafetyEnabled(true);


        


 
        
};

    
    
    @Override
    public void autonomousPeriodic(){
        
    
    }

    
    @Override
    public void teleopPeriodic()
    {
        m_follower_notifier.stop();
        leftSideDriveMotors.set(0);
        rightSideDriveMotors.set(0);

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

    private void followPath() {
        if (leftFollower.isFinished() || rightFollower.isFinished()) {
            m_follower_notifier.stop();
            } else {
            double left_speed = leftFollower.calculate(leftEncoder.get());
            double right_speed = rightFollower.calculate(rightEncoder.get());
            double heading = m_gyro.getAngle();
            double desired_heading = Pathfinder.r2d(leftFollower.getHeading());
            double heading_difference = Pathfinder.boundHalfDegrees(desired_heading - heading);
            double turn = 0.8 * (-1.0/80.0) * heading_difference;
            leftSideDriveMotors.set(left_speed + turn);
            rightSideDriveMotors.set(right_speed - turn);
            }
    }

}
