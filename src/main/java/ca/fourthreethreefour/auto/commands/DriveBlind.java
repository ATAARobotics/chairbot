package ca.fourthreethreefour.auto.commands;

import java.util.Vector;

import ca.fourthreethreefour.subsystems.Drive;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class DriveBlind extends TimedCommand {

    private Drive drive;
    double left;
    double right;

    public DriveBlind(Drive drive, double left, double right, double timeout) {
        super(timeout);
        this.drive = drive;
        this.left = left;
        this.right = right;
    }

    public DriveBlind(Drive drive, Vector<String[]> commandArgs) {
        super(10);
        this.drive = drive;
        try {
            left = Double.parseDouble(commandArgs.get(0)[0]);
        } catch (Exception e) {
            left = 0.8;
        }
        try {
            right = Double.parseDouble(commandArgs.get(0)[1]);
        } catch (Exception e) {
            right = 0.8;
        }
    }

    protected void execute() {
        drive.tankDrive(left, right);
    }

    // Called once after timeout
    @Override
    protected void end() {
        drive.tankDrive(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }

}
