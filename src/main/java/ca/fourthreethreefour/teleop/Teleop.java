package ca.fourthreethreefour.teleop;

import ca.fourthreethreefour.joysticks.HarmonixGuitarController;
import ca.fourthreethreefour.teleop.subsystems.Drive;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Teleop {

    public static Drive drive = new Drive();

    // Initialize an Xbox 360 controller to control the robot
    private XboxController controller = new XboxController(0);

    public void TeleopInit() {
        drive.driveInit();
    }

    public void TeleopPeriodic() {
        drive.drive(controller);

        Scheduler.getInstance().run();
    }
}