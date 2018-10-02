package main.java.ca.fourthreethreefour.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.first.command.Command;
import edu.first.commands.CommandGroup;
import edu.first.commands.common.LoopingCommand;
import edu.first.commands.common.SetOutput;
import edu.first.commands.common.WaitCommand;
import edu.first.module.actuators.DualActionSolenoid.Direction;
import edu.wpi.first.wpilibj.DriverStation;
import main.java.ca.fourthreethreefour.Robot;
import main.java.ca.fourthreethreefour.commands.CommandGroupFactory;
import main.java.ca.fourthreethreefour.commands.debug.Logging;
import main.java.ca.fourthreethreefour.subsystems.Drive;

// TODO Fix the spelling and capitalization.

public class AutoFile extends Robot implements Drive {

	/**
	 * List of all commands.
	 */
	public static final HashMap<String, RuntimeCommand> COMMANDS = new HashMap<>();

	/*
	 * Commands are registered in this section. To register a command, add it to the
	 * COMMANDS hashmap, with the key being the string in the associated .txt file
	 * that is associated with the command and the value being an instance of the
	 * command you wish to run when that string is found.
	 * 
	 */
	static {
		COMMANDS.put("print", new Print());
		COMMANDS.put("blinddrive", new BlindDrive());
		COMMANDS.put("stop", new Stop());
		COMMANDS.put("wait", new Wait());
		COMMANDS.put("waituntil", new WaitUntil());
		COMMANDS.put("setgear", new SetGear());
		//COMMANDS.put("driveupto", new DriveUpTo());
	}

	/**
	 * A timer. Determines how much time has passed between the beginning of the
	 * timeout and the current time. The timeout is how long the timer should run
	 * for.
	 * 
	 * @author Trevor Tsang, or maybe Joel
	 * @since 2017, lol
	 */
	private static class Timeout {
		private long start = 0;
		private long timeout; // how long the timer should run for

		public Timeout(long timeout) {
			this.timeout = timeout;
		}

		public boolean started() {
			return start != 0;
		}

		public void start() {
			// The time the timer starts at. Runs on Unix time.
			start = System.currentTimeMillis();
		}

		public boolean done() {
			// If the time passed since the start of the timer is longer than the timeout
			// value, return false
			return System.currentTimeMillis() - start > timeout;
		}
	}

	/**
	 * Constructor for a looping command using {@link Timeout} above to determine
	 * how long the command should run for.
	 * 
	 * @author Trevor, but maybe Joel actually
	 * @since 2017, lol
	 */
	private static abstract class LoopingCommandWithTimeout extends LoopingCommand implements Command {
		private Timeout timeout;

		public LoopingCommandWithTimeout(Timeout timeout) {
			this.timeout = timeout;
		}

		@Override
		public boolean continueLoop() {
			// If the driver station isn't in autonomous, or is disabled, stop the command
			// from looping
			if (!DriverStation.getInstance().isAutonomous() || !DriverStation.getInstance().isEnabled()) {
				Logging.log("Command interrupted");
				return false;
			}
			if (!timeout.started()) {
				timeout.start();
			}

			if (timeout.done()) {
				Logging.log("Command timed out");
			}
			
			return !timeout.done();
		}
	}

	/*
	 * Commands section! Add new commands here. To add new commands, create a new
	 * anonymous class that implements RuntimeCommand, and make getCommand() return
	 * a Command with your functionality in run(). PrintCommand is here as an
	 * example.
	 */

	/**
	 * Prints to the console, with the argument being the string to print.
	 * 
	 * @author Trevor or Joel, I forget
	 * @since 2017, lol
	 */
	private static class Print implements RuntimeCommand {
		@Override
		public Command getCommand(List<String> args) {
			return new Command() {
				@Override
				public void run() {
					System.out.println(args.toString());
				}
			};
		}
	}

	/**
	 * Tank drive command without sensor input.
	 * Arguments are (in order): speed, turn, duration
	 * of command
	 * 
	 * @author Trevor
	 *
	 */
	private static class BlindDrive implements RuntimeCommand {
		@Override
		public Command getCommand(List<String> args) {
			double speed = Double.parseDouble(args.get(0)),
					turn = Double.parseDouble(args.get(1));
			long time = Long.parseLong(args.get(2));
			return new LoopingCommandWithTimeout(new Timeout(time)) {
				@Override
				public void runLoop() {
					drivetrain.arcadeDrive(speed, turn);
				}

				@Override
				public void end() {
					drivetrain.stopMotor();
				}
			};
		}
	}

	/**
	 * Stops the bot
	 * 
	 * @author Cool, but originally either Trevor or Joel
	 * @since 2017
	 *
	 */
	private static class Stop implements RuntimeCommand {
		@Override
		public Command getCommand(List<String> args) {
			return new SetOutput(drivetrain.getDriveStraight(), 0); // Stops by setting drivetrain's drivestraight to 0.
		}
	}

	/**
	 * Waits with set value (double) ticks?
	 * 
	 * @author Cool, but originally either Trevor or Joel
	 * @since 2017
	 *
	 */
	private static class Wait implements RuntimeCommand {
		@Override
		public Command getCommand(List<String> args) {
			if (args.size() != 1) {
				// In case no arguments are put for Wait.
				throw new IllegalArgumentException("Error in Wait: Invalid arguments"); 
			} else {
				return new WaitCommand(Double.parseDouble(args.get(0))); // Runs the WaitCommand with value set.
			}
		}
	}

	/**
	 * Waits until set time (double) of match. Likely best used at beginning of
	 * match or end of match. Beginning to wait for other teams to move out of our
	 * way. End to end autonomous for the start of teleop.
	 * 
	 * @author Cool, but really either Trevor or Joel
	 * @since 2017
	 *
	 */
	private static class WaitUntil implements RuntimeCommand {

		@Override
		public Command getCommand(List<String> args) {
			double time = Double.parseDouble(args.get(0));
			return new Command() {

				@Override
				public void run() {
					// getMatchTime gives the time left in the current period, not how much time has passed.
					// That is why it's 15 - *.getMatchTime(), if the time left was 14, then it would be 1 second gone.
					while (15 - DriverStation.getInstance().getMatchTime() < time) {
						drivetrain.stopMotor();
					}
				}
			};
		}
	}
	
	/**
	 * Takes arguments for setting the gear.
	 * If 'low', then gearShifter will be set to LOW_GEAR.
	 * If 'high', then gearShifter will be set to HIGH_GEAR.
	 * 
	 * @author Cool
	 * @since 2018
	 *
	 */

	private static class SetGear implements RuntimeCommand, Drive {
		@Override
		public Command getCommand(List<String> args) {
			String gear = (args.get(0)).toLowerCase(); // Sets gear to the args 0 (arrays start at 0 I do believe) and sets it to lowercase so that no conflicts in capitalization.
			return new Command() {
				@Override
				public void run() {
					switch (gear) {
					case "low":
						Drive.gearShifter.set(LOW_GEAR);
						break;
					case "high":
						Drive.gearShifter.set(HIGH_GEAR);
						break;
					case "":
						Drive.gearShifter.set(Direction.OFF);
						throw new Error("Error in SetGear: No gear set");
					default:
						throw new Error("Error in SetGear: Gear set incorrectly");
					}
				}
			};
		}
	}
	
	// End of commands section

	// TODO Comment this section
	/**
	 * A set of two strings, key and value. Used to store commands.
	 * 
	 */
	public static class Entry {
		final String key, value;

		public Entry(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}

	/**
	 * List of all objects of type {@link Entry}.
	 */
	private List<Entry> entries = new ArrayList<>();
	/**
	 * List of all commands
	 */
	private Map<String, String> variables = new HashMap<>();

	public AutoFile(File file) throws IOException {
		String contents;
		try (FileInputStream fi = new FileInputStream(file)) {
			StringBuilder builder = new StringBuilder();
			int ch;
			while ((ch = fi.read()) != -1) {
				builder.append((char) ch);
			}
			contents = builder.toString();
		}

		/*
		 * Commands are separated by a new line. This means that every new line in the
		 * .txt file is a separate command.
		 */
		for (String line : contents.split("\n")) {
			if (line.trim().length() == 0 || line.trim().startsWith("//")) { // ignores blank lines or comments
				continue;
			} else if (line.contains("=")) {
				/*
				 * If the current line contains an '=' sign, everything before the '=' sign is
				 * the key, and everything after it is the value. Used to define values in the
				 * associated .txt file.
				 */
				String key = line.substring(0, line.indexOf('=') + 1).trim().toLowerCase();
				String value = line.substring(line.indexOf('=') + 1).trim().toLowerCase();
				variables.put(key, value);
			} else {
				// everything from the beginning of the line to the first '(' is the key
				String key = line.substring(0, line.indexOf('(')).trim().toLowerCase();
				// everything from after the first '(' to the last ')' is the value
				String value = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')')).trim().toLowerCase();

				// trims unnecessary brackets
				if (value.startsWith("(")) {
					value = value.substring(1);
				}
				if (value.endsWith(")")) {
					value = value.substring(0, value.length() - 1);
				}

				entries.add(new Entry(key, value));
			}
		}
	}

	/**
	 * Converts Strings in the associated .txt into commands, and arranges them to
	 * run in the order they are written in.
	 * 
	 * @return The commands
	 */
	public Command toCommand() {
		// an ArrayList of AutoFileCommands
		ArrayList<AutoFileCommand> commands = new ArrayList<>();
		for (Entry e : entries) { // for each Entry in the list of entries
			String value = e.value;
			for (Map.Entry<String, String> variable : variables.entrySet()) {
				if (value.contains(variable.getKey())) {
					value = value.replace(variable.getKey(), variable.getValue());
				}
			}
			commands.add(new AutoFileCommand(e.key, value));
		}

		// CommandGroupFactory is required because the constructor for CommandGroup is
		// private
		CommandGroup group = new CommandGroupFactory();
		for (AutoFileCommand command : commands) {
			if (COMMANDS.containsKey(command.name)) {
				if (command.concurrent) {
					group.appendConcurrent(COMMANDS.get(command.name).getCommand(command.arguments));
				} else {
					group.appendSequential(COMMANDS.get(command.name).getCommand(command.arguments));
				}
			} else {
				throw new Error(command.name + " not found");
			}
		}
		return group;
	}

	/**
	 * Reads the .txt file and turns lines into sets of commands and arguments.
	 * 
	 * @author Joel, actually
	 * @since 2017, lol
	 *
	 */
	private static class AutoFileCommand {
		public final boolean concurrent;
		public final String name;
		public final List<String> arguments;

		public AutoFileCommand(String key, String arguments) {
			if (key.startsWith("!")) { // if the line starts with '!', make it concurrent
				// concurrent commands run at the same time as the command before them
				this.concurrent = true;
				this.name = key.substring(1).toLowerCase();
			} else {
				// if commands are not concurrent, they will run in the order they are written
				this.concurrent = false;
				this.name = key.toLowerCase();
			}

			// trims brackets
			String inner = arguments;
			if (arguments.contains("(") && arguments.contains(")")) {
				inner = arguments.substring(arguments.indexOf('(') + 1, arguments.indexOf(')'));
			}
			// arguments are separated by ','
			this.arguments = Arrays.asList(inner.split(",")).stream().map(String::trim).collect(Collectors.toList());
		}
	}

	private interface RuntimeCommand {
		public Command getCommand(List<String> args);
	}

}