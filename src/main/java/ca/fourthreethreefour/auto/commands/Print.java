package ca.fourthreethreefour.auto.commands;

import java.util.Vector;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class Print extends InstantCommand {

    private String str;

    public Print(String str) {
        this.str = str;
    }

    public Print(Vector<String[]> commandArgs) {
        try {
            str = commandArgs.get(0)[0];
        } catch (Exception e) {
            str = "Test";
        }
    }

    public void execute() {
        System.out.println(str);
    }
}
