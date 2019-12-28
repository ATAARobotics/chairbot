package ca.fourthreethreefour.auto;

import java.io.File;

import ca.fourthreethreefour.auto.commands.DriveBlind;
import ca.fourthreethreefour.auto.commands.Print;
import ca.fourthreethreefour.subsystems.Drive;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Auto {

    private AutoFile test;
    private Command test2;
    private Drive drive;

    public Auto(Drive drive) {
        this.drive = drive;
        test2 = new Print("HITHIHTHTHITHTIHTIT");
    }

    public void AutoInit() {
        try {
            test = new AutoFile(new File("/auto.txt"), drive);
        } catch (Exception e) {
            System.out.println(e);
        }
        // test.run();
        test2.start();
    }

    public void AutoPeriodic() {
        Scheduler.getInstance().run();
        System.out.println(test2.isCompleted());
    }

    public void AutoFinished() {
    }

}