package ca.fourthreethreefour.auto;

import java.io.File;

import ca.fourthreethreefour.auto.commands.DriveBlind;
import ca.fourthreethreefour.auto.commands.Print;
import ca.fourthreethreefour.subsystems.Drive;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Auto {

    private AutoFile autoFile;
    private Drive drive;

    public Auto(Drive drive) {
        this.drive = drive;
    }

    public void AutoInit() {
        try {
            autoFile = new AutoFile(new File("/auto.txt"), drive);
        } catch (Exception e) {
            System.out.println("No valid text file detected.");
        }
        autoFile.init();
    }

    public void AutoPeriodic() {
        autoFile.run();
    }

    public void AutoFinished() {
    }

}