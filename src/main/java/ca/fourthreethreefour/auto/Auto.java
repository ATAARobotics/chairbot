package ca.fourthreethreefour.auto;

import ca.fourthreethreefour.auto.groups.TestGroup;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Auto {

    Command testCommand = new TestGroup();

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