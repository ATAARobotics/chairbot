package ca.fourthreethreefour.commands.debug;

import edu.wpi.first.networktables.NetworkTableEntry;

public class Logging {

    public static void put(NetworkTableEntry key, double value, boolean enabled) {
        if (enabled) {
            
            //ShuffleboardTab tab = Shuffleboard.getTab("Vision");
            //NetworkTableEntry distanceEntry = tab.add("Distance to target", 0).getEntry();
            key.setDouble(value);

            //edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putNumber(key, value);
        }
    }
    
    public static void put(NetworkTableEntry key, String value, boolean enabled) {
        if (enabled) {
            key.setString(value);
            //edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putString(key, value);
        }
    }
    
    public static void put(NetworkTableEntry key, boolean value, boolean enabled) {
        if (enabled) {
            key.setBoolean(value);
            //edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putBoolean(key, value);
        }
    }
    
    public static void log(String str) {
            System.out.println(str);
    }

    public static void log(String str, boolean enabled) {
        if (enabled) {
            System.out.println(str);
        }
    }
    
    public static void logf(String format, Object... args) {
            System.out.printf(format + "\n", args);
    }

    public static void logf(String format, boolean enabled, Object... args) {
        if (enabled) {
            System.out.printf(format + "\n", args);
        }
    }
}
