package ca.fourthreethreefour.subsystems;

import edu.first.module.Module;
import edu.first.module.subsystems.Subsystem;
import edu.first.module.actuators.MotorModule;
import ca.fourthreethreefour.settings.Settings;

public interface SpeedControllers extends Settings {
	
	MotorModule
		speedController1 = new MotorModule(TYPE_SPEED_CONTROLLER_1, SPEED_CONTROLLER_1),
		speedController2 = new MotorModule(TYPE_SPEED_CONTROLLER_2, SPEED_CONTROLLER_2),
		speedController3 = new MotorModule(TYPE_SPEED_CONTROLLER_3, SPEED_CONTROLLER_3),
		speedController4 = new MotorModule(TYPE_SPEED_CONTROLLER_4, SPEED_CONTROLLER_4);
	
	public Subsystem speedcontrollers = new Subsystem(new Module[] { speedController1, speedController2, speedController3, speedController4 });
	
}
