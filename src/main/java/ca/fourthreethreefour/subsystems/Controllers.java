package ca.fourthreethreefour.subsystems;

import ca.fourthreethreefour.module.joysticks.JoystickController;
import ca.fourthreethreefour.settings.Settings;
import edu.first.module.Module;
import edu.first.module.subsystems.Subsystem;

public interface Controllers extends Settings {
	
	JoystickController
		controller = new JoystickController(0);
	
	Subsystem controllers = new Subsystem(new Module[] { controller});
}
