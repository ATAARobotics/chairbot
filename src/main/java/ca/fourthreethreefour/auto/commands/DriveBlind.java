package ca.fourthreethreefour.auto.commands;

import ca.fourthreethreefour.teleop.Teleop;
import ca.fourthreethreefour.teleop.subsystems.Drive;
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

    protected void execute() {
        Drive.extDrive(left, right);
    }

    // Called once after timeout
    @Override
    protected void end() {
        Drive.extDrive(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }

}
