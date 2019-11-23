package ca.fourthreethreefour.auto.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class Print extends InstantCommand {

    private final String str;

    public Print(String str) {
        this.str = str;
    }

    public void execute() {
        System.out.println(str);
    }
}
