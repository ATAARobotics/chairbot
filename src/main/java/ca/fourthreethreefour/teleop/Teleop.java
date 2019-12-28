package ca.fourthreethreefour.teleop;

import ca.fourthreethreefour.subsystems.Drive;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Teleop {

    // Initialize an Xbox 360 controller to control the robot
    private XboxController controller = new XboxController(0);

    private Drive drive;

    public Teleop(Drive drive) {
        this.drive = drive;
    }

    public void TeleopInit() {
        drive.driveInit();
        drive.encoderReset();
    }

    public void TeleopPeriodic() {
        double speed = -controller.getY(Hand.kLeft);
        double turn = controller.getX(Hand.kRight);
        turn += (speed > 0) ? 0 : (speed < 0) ? -0 : 0;
        drive.arcadeDrive(speed, turn, true);

        drive.encoderPrint();

        Scheduler.getInstance().run();
    }
    
}