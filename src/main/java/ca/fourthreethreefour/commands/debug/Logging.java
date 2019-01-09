package ca.fourthreethreefour.commands.debug;

import ca.fourthreethreefour.Robot;
import edu.wpi.first.networktables.NetworkTableEntry;

public class Logging {

    

    public static void put(NetworkTableEntry key, double value) {
        if (Robot.LOGGING_ENABLED) {
            
            //ShuffleboardTab tab = Shuffleboard.getTab("Vision");
            //NetworkTableEntry distanceEntry = tab.add("Distance to target", 0).getEntry();
            key.setDouble(value);

            //edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putNumber(key, value);
        }
    }
    
    public static void put(NetworkTableEntry key, String value) {
        if (Robot.LOGGING_ENABLED) {
            key.setString(value);
            //edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putString(key, value);
        }
    }
    
    public static void put(NetworkTableEntry key, boolean value) {
        if (Robot.LOGGING_ENABLED) {
            key.setBoolean(value);
            //edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putBoolean(key, value);
        }
    }

    public static void log(String str) {
        if (Robot.LOGGING_ENABLED) {
            System.out.println(str);
        }
    }
    
    public static void logf(String format, Object... args) {
        if (Robot.LOGGING_ENABLED) {
            System.out.printf(format + "\n", args);
        }
    }
}

