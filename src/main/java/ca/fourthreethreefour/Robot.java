/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour;

import ca.fourthreethreefour.auto.Auto;
import ca.fourthreethreefour.teleop.Teleop;
import ca.fourthreethreefour.teleop.subsystems.Drive;
import ca.fourthreethreefour.teleop.subsystems.Encoder;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

// If you rename or move this class, update the build.properties file in the project root
public class Robot extends TimedRobot implements Constants
{

    static private Drive drive;
    static private Encoder encoder;
    static private Teleop teleop;
    static private Auto auto;

    ShuffleboardTab dynamicSettingsTab = Shuffleboard.getTab("Dynamic Settings");
	    NetworkTableEntry LOGGING_ENABLED_ENTRY = dynamicSettingsTab.addPersistent("Logging", true).withWidget(BuiltInWidgets.kToggleSwitch).getEntry();
    static public boolean LOGGING_ENABLED;

    @Override
    public void robotInit() {
        drive = new Drive();
        encoder = new Encoder(drive);
        teleop = new Teleop(drive, encoder);
        auto = new Auto(drive, encoder);
    }

    @Override
    public void robotPeriodic() {

    }

    @Override
    public void autonomousInit() {
        auto.AutoInit();
    }

    @Override
    public void autonomousPeriodic() {
        auto.AutoPeriodic();
    }

    @Override
    public void teleopInit() {
        auto.AutoFinished();
        teleop.TeleopInit();
    }

    @Override
    public void teleopPeriodic() {
        teleop.TeleopPeriodic();
    }

    @Override
    public void disabledPeriodic() {
        updateSettings();
    }

    @Override
    public void testPeriodic() {

    }

    public void updateSettings() {
        LOGGING_ENABLED = LOGGING_ENABLED_ENTRY.getBoolean(true);
    }
}
