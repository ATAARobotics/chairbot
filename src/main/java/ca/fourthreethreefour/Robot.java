/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

// If you rename or move this class, update the build.properties file in the project root
public class Robot extends TimedRobot implements Constants
{
    // Initialize an Xbox 360 controller to control the robot
    private XboxController primaryController;

    // Initialize the drivetrain motors
    private WPI_TalonSRX gearMotor;
    private WPI_TalonSRX leftDriveMotor2;
    //private WPI_TalonSRX rightDriveMotor1;
    private WPI_TalonSRX rightDriveMotor2;

    // Pairs up the drivetrain motors based on their respective side and initializes the drivetrain controlling object
    private SpeedControllerGroup leftSideDriveMotors;
    private SpeedControllerGroup rightSideDriveMotors;
    private DifferentialDrive robotDrive;

    
	ShuffleboardTab settingsTab = Shuffleboard.getTab("Settings");
        NetworkTableEntry DRIVE_SPEED_ENTRY = settingsTab.addPersistent("DRIVE_SPEED", 0.5).getEntry();
            double DRIVE_SPEED;
        NetworkTableEntry TURN_CURVE_ENTRY = settingsTab.addPersistent("TURN_CURVE", 1.5).getEntry();
            double TURN_CURVE;

    @Override
    public void robotInit()
    {
        // Assigns all the motors to their respective objects (the number in brackets is the port # of what is connected where)
        primaryController = new XboxController(0);
        
        gearMotor = new WPI_TalonSRX(2);
        leftDriveMotor2 = new WPI_TalonSRX(1);
        //rightDriveMotor1 = new WPI_TalonSRX(2);
        rightDriveMotor2 = new WPI_TalonSRX(3);

        // Assigns the drivetrain motors to their respective motor controller group and then passes them on to the drivetrain controller object
        leftSideDriveMotors = new SpeedControllerGroup(leftDriveMotor2);
        rightSideDriveMotors = new SpeedControllerGroup(rightDriveMotor2);
        robotDrive = new DifferentialDrive(leftSideDriveMotors, rightSideDriveMotors);

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
        
        // Sends the Y axis input from the left stick (speed) and the X axis input from the right stick (rotation) from the primary controller to move the robot
        robotDrive.arcadeDrive(primaryController.getY(GenericHID.Hand.kLeft)*DRIVE_SPEED, -primaryController.getX(GenericHID.Hand.kRight));
        // robotDrive.arcadeDrive(primaryController.getY(GenericHID.Hand.kRight), -primaryController.getX(GenericHID.Hand.kLeft));
        //leftDriveMotor1.set(primaryController.getTriggerAxis(GenericHID.Hand.kRight));
        gearMotor.set(-primaryController.getTriggerAxis(GenericHID.Hand.kRight));
       
    }

    @Override
    public void disabledPeriodic() {
        updateSettings();
        System.out.println(DRIVE_SPEED);
    }

    @Override
    public void testPeriodic(){

    }

    public void updateSettings() {
       DRIVE_SPEED = DRIVE_SPEED_ENTRY.getDouble(0.5);
       TURN_CURVE = TURN_CURVE_ENTRY.getDouble(1.5);
    }
}
