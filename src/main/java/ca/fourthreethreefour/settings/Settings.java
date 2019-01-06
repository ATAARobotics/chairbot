package ca.fourthreethreefour.settings;

import java.io.File;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/**
 * Contains all values {@link SettingsFile} can parse, as well as their keys and default values.
 * @author Trevor, but also Joel
 * 
 */
public interface Settings {
	
	
	// public static Type toType(String type) {
	// 	if (type.equalsIgnoreCase("talon_srx") || type.equalsIgnoreCase("talonsrx")) {
	// 		return Type.TALON_SRX;
	// 	}
		
	// 	if (type.equalsIgnoreCase("victor_spx") || type.equalsIgnoreCase("victorspx")) {
	// 		return Type.VICTOR_SPX;
	// 	}
		
	// 	return null;
	// }
	
	ShuffleboardTab settingsTab = Shuffleboard.getTab("Settings");

	NetworkTableEntry LOGGING_ENABLED_ENTRY = settingsTab.add("LOGGING_ENABLED", false).getEntry();
		boolean LOGGING_ENABLED = LOGGING_ENABLED_ENTRY.getBoolean(false);
	
	NetworkTableEntry TURN_CURVE_ENTRY = settingsTab.add("TURN_CURVE", 1.5).getEntry();
		double TURN_CURVE = TURN_CURVE_ENTRY.getDouble(1.5);
	
	NetworkTableEntry DRIVE_SPEED_ENTRY = settingsTab.add("DRIVE_SPEED", 0.5).getEntry();
		double DRIVE_SPEED = DRIVE_SPEED_ENTRY.getDouble(0.5);
	NetworkTableEntry TURN_SPEED_ENTRY = settingsTab.add("TURN_SPEED", 1).getEntry();
		double TURN_SPEED = TURN_SPEED_ENTRY.getDouble(1);

    // Ports
	/*
		NetworkTableEntry EXAMPLE_PORT_ENTRY = settingsTab.add("EXAMPLE_PORT", [default port]).getEntry();
			int EXAMPLE_PORT = (int) EXAMPLE_PORT_ENTRY.getDouble([default port]);
	*/

	NetworkTableEntry DRIVE_LEFT_1_ENTRY = settingsTab.add("DRIVE_LEFT_1", 0).getEntry();
		int DRIVE_LEFT_1 = (int) DRIVE_LEFT_1_ENTRY.getDouble(0);
	NetworkTableEntry DRIVE_LEFT_2_ENTRY = settingsTab.add("DRIVE_LEFT_2", 1).getEntry();
		int DRIVE_LEFT_2 = (int) DRIVE_LEFT_2_ENTRY.getDouble(1);
	NetworkTableEntry DRIVE_LEFT_3_ENTRY = settingsTab.add("DRIVE_LEFT_3", 2).getEntry();
		int DRIVE_LEFT_3 = (int) DRIVE_LEFT_3_ENTRY.getDouble(2);
	NetworkTableEntry DRIVE_RIGHT_1_ENTRY = settingsTab.add("DRIVE_RIGHT_1", 3).getEntry();
		int DRIVE_RIGHT_1 = (int) DRIVE_RIGHT_1_ENTRY.getDouble(3);
	NetworkTableEntry DRIVE_RIGHT_2_ENTRY = settingsTab.add("DRIVE_RIGHT_2", 4).getEntry();
		int DRIVE_RIGHT_2 = (int) DRIVE_RIGHT_2_ENTRY.getDouble(4);
	NetworkTableEntry DRIVE_RIGHT_3_ENTRY = settingsTab.add("DRIVE_RIGHT_3", 5).getEntry();
		int DRIVE_RIGHT_3 = (int) DRIVE_RIGHT_3_ENTRY.getDouble(5);

	NetworkTableEntry GEAR_SHIFTER_SOLENOID_1_ENTRY = settingsTab.add("GEAR_SHIFTER_SOLENOID_1", 6).getEntry();
		int GEAR_SHIFTER_SOLENOID_1 = (int) GEAR_SHIFTER_SOLENOID_1_ENTRY.getDouble(6);
	NetworkTableEntry GEAR_SHIFTER_SOLENOID_2_ENTRY = settingsTab.add("GEAR_SHIFTER_SOLENOID_2", 7).getEntry();
		int GEAR_SHIFTER_SOLENOID_2 = (int) GEAR_SHIFTER_SOLENOID_2_ENTRY.getDouble(7);

	NetworkTableEntry SPEED_CONTROLLER_1_ENTRY = settingsTab.add("SPEED_CONTROLLER_1", 6).getEntry();
		int SPEED_CONTROLLER_1 = (int) SPEED_CONTROLLER_1_ENTRY.getDouble(6);
	NetworkTableEntry SPEED_CONTROLLER_2_ENTRY = settingsTab.add("SPEED_CONTROLLER_2", 7).getEntry();
		int SPEED_CONTROLLER_2 = (int) SPEED_CONTROLLER_2_ENTRY.getDouble(7);
	NetworkTableEntry SPEED_CONTROLLER_3_ENTRY = settingsTab.add("SPEED_CONTROLLER_3", 8).getEntry();
		int SPEED_CONTROLLER_3 = (int) SPEED_CONTROLLER_3_ENTRY.getDouble(8);
	NetworkTableEntry SPEED_CONTROLLER_4_ENTRY = settingsTab.add("SPEED_CONTROLLER_4", 9).getEntry();
		int SPEED_CONTROLLER_4 = (int) SPEED_CONTROLLER_4_ENTRY.getDouble(9);
	
	// NetworkTableEntry TYPE_DRIVE_LEFT_1_ENTRY = settingsTab.add("TYPE_DRIVE_LEFT_1", "talonsrx").getEntry();
	// 	MotorModule.Type TYPE_DRIVE_LEFT_1 = toType(TYPE_DRIVE_LEFT_1_ENTRY.getString("talonsrx"));
	// NetworkTableEntry TYPE_DRIVE_LEFT_2_ENTRY = settingsTab.add("TYPE_DRIVE_LEFT_2", "talonsrx").getEntry();
	// 	MotorModule.Type TYPE_DRIVE_LEFT_2 = toType(TYPE_DRIVE_LEFT_2_ENTRY.getString("talonsrx"));
	// NetworkTableEntry TYPE_DRIVE_LEFT_3_ENTRY = settingsTab.add("TYPE_DRIVE_LEFT_3", "talonsrx").getEntry();
	// 	MotorModule.Type TYPE_DRIVE_LEFT_3 = toType(TYPE_DRIVE_LEFT_3_ENTRY.getString("talonsrx"));
	// NetworkTableEntry TYPE_DRIVE_RIGHT_1_ENTRY = settingsTab.add("TYPE_DRIVE_RIGHT_1", "talonsrx").getEntry();
	// 	MotorModule.Type TYPE_DRIVE_RIGHT_1 = toType(TYPE_DRIVE_RIGHT_1_ENTRY.getString("talonsrx"));
	// NetworkTableEntry TYPE_DRIVE_RIGHT_2_ENTRY = settingsTab.add("TYPE_DRIVE_RIGHT_2", "talonsrx").getEntry();
	// 	MotorModule.Type TYPE_DRIVE_RIGHT_2 = toType(TYPE_DRIVE_RIGHT_2_ENTRY.getString("talonsrx"));
	// NetworkTableEntry TYPE_DRIVE_RIGHT_3_ENTRY = settingsTab.add("TYPE_DRIVE_RIGHT_3", "talonsrx").getEntry();
	// 	MotorModule.Type TYPE_DRIVE_RIGHT_3 = toType(TYPE_DRIVE_RIGHT_3_ENTRY.getString("talonsrx"));

	// NetworkTableEntry TYPE_SPEED_CONTROLLER_1_ENTRY = settingsTab.add("TYPE_SPEED_CONTROLLER_1", "talonsrx").getEntry();
	// 	MotorModule.Type TYPE_SPEED_CONTROLLER_1 = toType(TYPE_SPEED_CONTROLLER_1_ENTRY.getString("talonsrx"));
	// NetworkTableEntry TYPE_SPEED_CONTROLLER_2_ENTRY = settingsTab.add("TYPE_SPEED_CONTROLLER_2", "talonsrx").getEntry();
	// 	MotorModule.Type TYPE_SPEED_CONTROLLER_2 = toType(TYPE_SPEED_CONTROLLER_2_ENTRY.getString("talonsrx"));
	// NetworkTableEntry TYPE_SPEED_CONTROLLER_3_ENTRY = settingsTab.add("TYPE_SPEED_CONTROLLER_3", "talonsrx").getEntry();
	// 	MotorModule.Type TYPE_SPEED_CONTROLLER_3 = toType(TYPE_SPEED_CONTROLLER_3_ENTRY.getString("talonsrx"));
	// NetworkTableEntry TYPE_SPEED_CONTROLLER_4_ENTRY = settingsTab.add("TYPE_SPEED_CONTROLLER_4", "talonsrx").getEntry();
	// 	MotorModule.Type TYPE_SPEED_CONTROLLER_4 = toType(TYPE_SPEED_CONTROLLER_4_ENTRY.getString("talonsrx"));
	
	NetworkTableEntry XBOXCONTROLLER_ENTRY = settingsTab.add("XBOXCONTROLLER", 0).getEntry();
		int XBOXCONTROLLER = (int) XBOXCONTROLLER_ENTRY.getDouble(0);
	
	NetworkTableEntry DRIVE_COMPENSATION_ENTRY = settingsTab.add("DRIVE_COMPENSATION", 0).getEntry();
		double DRIVE_COMPENSATION = DRIVE_COMPENSATION_ENTRY.getDouble(0);
}
