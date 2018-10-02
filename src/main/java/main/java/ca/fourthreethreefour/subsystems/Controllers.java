package main.java.ca.fourthreethreefour.subsystems;

import main.java.ca.fourthreethreefour.settings.Settings;
import edu.first.module.Module;
import edu.first.module.joysticks.XboxController;
import edu.first.module.subsystems.Subsystem;

public interface Controllers extends Settings {
	
	XboxController
		controller = new XboxController(XBOXCONTROLLER);
	
	Subsystem controllers = new Subsystem(new Module[] { controller});
}
