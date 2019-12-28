package ca.fourthreethreefour.auto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import ca.fourthreethreefour.auto.commands.DriveBlind;
import ca.fourthreethreefour.auto.commands.Print;
import ca.fourthreethreefour.subsystems.Drive;
import edu.wpi.first.wpilibj.command.Command;

public class AutoFile {
    Drive drive;
    /**
     * The current command, -1 signifies that none have been run.
     */
    private int currentCommand = -1;

    public static final HashMap<String, Command> COMMANDS = new HashMap<>();
    private static Vector<String[]> commandArgs = new Vector<>();

    {
        COMMANDS.put("driveblind", new DriveBlind(drive, commandArgs));
        COMMANDS.put("print", new Print(commandArgs));
    }


    // private static class DriveBlindCommand implements RuntimeCommand {
    //     public DriveBlindCommand() {
    //         new DriveBlind(drive, Double.parseDouble(commandArgs.get(0)), Double.parseDouble(commandArgs.get(1)), Double.parseDouble(commandArgs.get(2)));
    //     }
    // }

    // private class PrintCommand implements RuntimeCommand {
    //     public PrintCommand(Vector<String[]> commandArgs) {
    //         String string;
    //         try {
    //             string = commandArgs.get(0)[0];
    //         } catch (Exception e) {
    //             string = "Test";
    //         }
    //         new Print(string);
    //     }
    // }

    public static class Entry {
        final Command e_key;
        final int e_state;
        static final int CONCURRENT = 0;
        static final int SEQUENTIAL = 1;

        public Entry(Command key, int state) {
            this.e_key = key;
            this.e_state = state;
        }
    }

    private Vector<Entry> commandsParents = new Vector<>();
    private Vector<Entry> commandsChildren = new Vector<>();

    public AutoFile(File file, Drive drive) throws IOException {
        this.drive = drive;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String currentLine;
        Vector<String> contents = new Vector<>();
        while ((currentLine = bufferedReader.readLine()) != null) {
            contents.addElement(currentLine);
        }
        bufferedReader.close();

        for (int i = 0; i < contents.size(); i++) {
            final int state;
            if (contents.get(i).charAt(0) == '!') {
                state = Entry.CONCURRENT;
                contents.setElementAt(contents.get(i).substring(1), i);
            } else {
                state = Entry.SEQUENTIAL;
            }
            final Command command;
            String key = contents.get(i).substring(0, contents.get(i).indexOf("(")).toLowerCase();
            if (COMMANDS.containsKey(key)) {
                command = COMMANDS.get(key);
            } else {
                throw new Error(key + " is not a valid command!");
            }
            contents.setElementAt(contents.get(i).substring(contents.get(i).indexOf("(")+1, contents.get(i).length()-1), i);
            commandArgs.add(contents.get(i).split(","));
            commandsParents.addElement(new Entry(command, state));
        }
    }

    public void run() {
        Entry entry = null;
        Command cmd = null;
        boolean firstRun = false;
        if (currentCommand == -1) {
          firstRun = true;
          currentCommand = 0;
        }

        while (currentCommand < commandsParents.size()) {
            entry = commandsParents.elementAt(currentCommand);
            cmd = entry.e_key;
            if (firstRun) {
                cmd.start();
                firstRun = false;
            }
            if (cmd.isCompleted()) {
                firstRun = true;
                currentCommand++;
            }
            System.out.println("Hit!");
            // if (cmd != null) {
            //     if (!cmd.isRunning()) {
            //         System.out.println("Done");
            //         cmd.cancel();
            //         currentCommand++;
            //         firstRun = true;
            //         cmd = null;
            //         continue;
            //     } else {
            //         System.out.println("Not Done!");
            //     }
            // }

            // entry = commandsParents.elementAt(currentCommand);
            // cmd = null;

            // switch (entry.e_state) {
            //     case Entry.SEQUENTIAL:
            //         cmd = entry.e_key;
            //         if (firstRun) {
            //             cmd.start();
            //         }
            //         firstRun = false;
            //         break;
            //     case Entry.CONCURRENT:
            //         currentCommand++;
            //         entry.e_key.start();
            //         commandsChildren.addElement(entry);
            //         break;
            // }
        }

        for (int i = 0; i < commandsChildren.size(); i++) {
            entry = commandsChildren.elementAt(i);
            Command child = entry.e_key;
            if (!child.isRunning()) {
                child.cancel();
                commandsChildren.removeElementAt(i--);
            }
        }

    }

    // private interface RuntimeCommand {
	// }

}