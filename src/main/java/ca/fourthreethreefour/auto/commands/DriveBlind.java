package ca.fourthreethreefour.auto.commands;

import ca.fourthreethreefour.teleop.Teleop;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class DriveBlind extends TimedCommand {

    double left;
    double right;

    public DriveBlind(double timeout) {
        super(timeout);
    }

    public DriveBlind(double left, double right, double timeout) {
        super(timeout);
        this.left = left;
        this.right = right;
    }

    protected void execute() {
        Teleop.ExtDrive(left, right);
    }

    // Called once after timeout
    @Override
    protected void end() {
        Teleop.ExtDrive(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }

}
