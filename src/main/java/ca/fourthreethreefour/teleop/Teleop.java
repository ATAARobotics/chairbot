package ca.fourthreethreefour.teleop;

import ca.fourthreethreefour.joysticks.HarmonixGuitarController;
import ca.fourthreethreefour.teleop.subsystems.Drive;
import ca.fourthreethreefour.teleop.subsystems.Encoder;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Teleop {


    // Initialize an Xbox 360 controller to control the robot
    private XboxController controller = new XboxController(0);

    private Drive drive;
    private Encoder encoder;

    public Teleop(Drive drive, Encoder encoder) {
        this.drive = drive;
        this.encoder = encoder;
    }

    public void TeleopInit() {
        drive.driveInit();
        encoder.reset();
    }

    public void TeleopPeriodic() {
        drive.drive(controller);
        encoder.print();
        Scheduler.getInstance().run();
    }

    /**
    * Drive function for external use
    * @param leftValue value for left motors, ranges from 1 to -1
    * @param rightValue value for right motors, ranges from 1 to -1
    * @return void
    */
    public void ExtDrive(double leftValue, double rightValue) {
        drive.extDrive(leftValue, rightValue);
    }

    /**
    * ArcadeDrive for external use
    * @param speed value for both motors, ranges from 1 to -1
    * @param angle value for motors, ranges from 1 to -1
    */
    public void ExtArcadeDrive(double speed, double angle){
        drive.extArcadeDrive(speed, angle);
    }

    
}