package ca.fourthreethreefour.auto;

import edu.wpi.first.wpilibj.command.Command;

public class Auto {

    Command testCommand = new TestGroup();

    public void AutoInit() {
        testCommand.start();
    }

}