package ca.fourthreethreefour.auto;

import ca.fourthreethreefour.auto.groups.TestGroup;
import ca.fourthreethreefour.subsystems.Drive;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Auto {

    private Command testCommand;

    public Auto(Drive drive) {
        testCommand = new TestGroup(drive);
    }

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