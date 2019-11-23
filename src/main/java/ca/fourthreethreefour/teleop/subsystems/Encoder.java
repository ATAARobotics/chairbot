package ca.fourthreethreefour.teleop.subsystems;

import com.revrobotics.CANEncoder;

import ca.fourthreethreefour.commands.debug.Logging;
import ca.fourthreethreefour.wrapper.CANEncoderWrapper;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Encoder extends Subsystem {

    private static CANEncoderWrapper frontLeftEncoder;
    private static CANEncoderWrapper rearLeftEncoder;
    private static CANEncoderWrapper frontRightEncoder;
    private static CANEncoderWrapper rearRightEncoder;

    @Override
    protected void initDefaultCommand() {

    }

    public Encoder(Drive drive) {
        frontLeftEncoder = new CANEncoderWrapper(drive.frontLeftMotor);
        rearLeftEncoder = new CANEncoderWrapper(drive.rearLeftMotor);
        frontRightEncoder = new CANEncoderWrapper(drive.frontRightMotor);
        rearRightEncoder = new CANEncoderWrapper(drive.rearRightMotor);
    }

    public static void reset() {
        frontLeftEncoder.reset();
        rearLeftEncoder.reset();
        frontRightEncoder.reset();
        rearRightEncoder.reset();
        System.out.println("Hit");
    }

    public static void print() {
        Logging.logf("Average: %.2f Left encoder: %.2f Right encoder: %.2f", getAverage(), getLeftEncoder(), getRightEncoder());
    }

    public static double getLeftEncoder() {
        return (frontLeftEncoder.get() * rearLeftEncoder.get()) / 2;
    }

    public static double getRightEncoder() {
        return (frontRightEncoder.get() * rearRightEncoder.get()) / 2;
    }

    public static double getAverage() {
        return (getRightEncoder() + getLeftEncoder()) / 2;
    }

}