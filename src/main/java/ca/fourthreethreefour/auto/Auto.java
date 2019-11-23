package ca.fourthreethreefour.auto;

import ca.fourthreethreefour.auto.groups.TestGroup;
import ca.fourthreethreefour.teleop.subsystems.Drive;
import ca.fourthreethreefour.teleop.subsystems.Encoder;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Auto {

    private Encoder encoder;
    private Drive drive;

    public Auto(Drive drive, Encoder encoder) {
        this.encoder = encoder;
        this.drive = drive;
    }

    Command testCommand = new TestGroup(drive, encoder);

    public void AutoInit() {
        testCommand.start();
    }

    public void AutoPeriodic() {
        Scheduler.getInstance().run();
    }

    public void AutoFinished() {
        testCommand.cancel();
    }

}