package main.java.ca.fourthreethreefour.settings;

import java.io.File;

import main.java.ca.fourthreethreefour.module.actuators.MotorModule;
import main.java.ca.fourthreethreefour.module.actuators.MotorModule.Type;

/**
 * Contains all values {@link SettingsFile} can parse, as well as their keys and default values.
 * @author Trevor, but also Joel
 * 
 */
public interface Settings {
	
	
	public static Type toType(String type) {
		if (type.equalsIgnoreCase("talon_srx") || type.equalsIgnoreCase("talonsrx")) {
			return Type.TALON_SRX;
		}
		
		if (type.equalsIgnoreCase("victor_spx") || type.equalsIgnoreCase("victorspx")) {
			return Type.VICTOR_SPX;
		}
		
		return null;
	}

	
	SettingsFile settingsFile = new SettingsFile(new File("/settings.txt"));
	
    boolean LOGGING_ENABLED = settingsFile.getBooleanProperty("LOGGING_ENABLED", false);
	
    double TURN_CURVE = settingsFile.getDoubleProperty("TURN_CURVE", 1.5);

    // Ports
	// TODO get default ports
	// int EXAMPLE_PORT = settingsFile.getIntProperty("EXAMPLE_PORT", [default port])
	int DRIVE_LEFT_1 = settingsFile.getIntProperty("DRIVE_LEFT_1", 0);
	int DRIVE_LEFT_2 = settingsFile.getIntProperty("DRIVE_LEFT_2", 1);
	int DRIVE_RIGHT_1 = settingsFile.getIntProperty("DRIVE_RIGHT_1", 2);
	int DRIVE_RIGHT_2 = settingsFile.getIntProperty("DRIVE_RIGHT_2", 3);

	int GEAR_SHIFTER_SOLENOID_1 = settingsFile.getIntProperty("GEAR_SHIFTER_SOLENOID_1", 0);
	int GEAR_SHIFTER_SOLENOID_2 = settingsFile.getIntProperty("GEAR_SHIFTER_SOLENOID_2", 1);

	int SPEED_CONTROLLER_1 = settingsFile.getIntProperty("SPEED_CONTROLLER_1", 7);
	int SPEED_CONTROLLER_2 = settingsFile.getIntProperty("SPEED_CONTROLLER_2", 4);
	int SPEED_CONTROLLER_3 = settingsFile.getIntProperty("SPEED_CONTROLLER_3", 6);
	int SPEED_CONTROLLER_4 = settingsFile.getIntProperty("SPEED_CONTROLLER_4", 5);
	
	MotorModule.Type TYPE_DRIVE_LEFT_1 = toType(settingsFile.getProperty("TYPE_DRIVE_LEFT_1", "talonsrx"));
	MotorModule.Type TYPE_DRIVE_LEFT_2 = toType(settingsFile.getProperty("TYPE_DRIVE_LEFT_2", "talonsrx"));
	MotorModule.Type TYPE_DRIVE_RIGHT_1 = toType(settingsFile.getProperty("TYPE_DRIVE_RIGHT_1", "talonsrx"));
	MotorModule.Type TYPE_DRIVE_RIGHT_2 = toType(settingsFile.getProperty("TYPE_DRIVE_RIGHT_2", "talonsrx"));

	MotorModule.Type TYPE_SPEED_CONTROLLER_1 = toType(settingsFile.getProperty("TYPE_SPEED_CONTROLLER_1", "talonsrx"));
	MotorModule.Type TYPE_SPEED_CONTROLLER_2 = toType(settingsFile.getProperty("TYPE_SPEED_CONTROLLER_2", "talonsrx"));
	MotorModule.Type TYPE_SPEED_CONTROLLER_3 = toType(settingsFile.getProperty("TYPE_SPEED_CONTROLLER_3", "talonsrx"));
	MotorModule.Type TYPE_SPEED_CONTROLLER_4 = toType(settingsFile.getProperty("TYPE_SPEED_CONTROLLER_4", "talonsrx"));
	
	int XBOXCONTROLLER = settingsFile.getIntProperty("XBOXCONTROLLER", 0);
	
	double DRIVE_COMPENSATION = settingsFile.getDoubleProperty("DRIVE_COMPENSATION", 0);
}
