package ca.fourthreethreefour.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import ca.fourthreethreefour.commands.debug.Logging;
import ca.fourthreethreefour.wrapper.CANEncoderWrapper;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drive extends Subsystem {

    public WPI_VictorSPX frontLeftMotor;
    public WPI_VictorSPX rearLeftMotor;
    public WPI_VictorSPX frontRightMotor;
    public WPI_VictorSPX rearRightMotor;

    // Initialize the drivetrain motors
    // private WPI_TalonSRX leftDriveMotor = new WPI_TalonSRX(0);
    // private WPI_TalonSRX rightDriveMotor = new WPI_TalonSRX(1);

    // Pairs up the drivetrain motors based on their respective side and initializes the drivetrain controlling object
    private SpeedControllerGroup leftSideDriveMotors;
    private SpeedControllerGroup rightSideDriveMotors;
    private DifferentialDrive robotDrive;

    // private CANEncoderWrapper frontLeftEncoder;
    // private CANEncoderWrapper rearLeftEncoder;
    // private CANEncoderWrapper frontRightEncoder;
    // private CANEncoderWrapper rearRightEncoder;
    private Encoder leftEncoder;
    private Encoder rightEncoder;
    // Sets the appropriate configuration settings for the motors

    private AHRS navX;

    public Drive() {
        frontLeftMotor = new WPI_VictorSPX(0);
        rearLeftMotor = new WPI_VictorSPX(1);
        frontRightMotor = new WPI_VictorSPX(2);
        rearRightMotor = new WPI_VictorSPX(3);

        leftSideDriveMotors = new SpeedControllerGroup(frontLeftMotor, rearLeftMotor);
        rightSideDriveMotors = new SpeedControllerGroup(frontRightMotor, rearRightMotor);
        robotDrive = new DifferentialDrive(leftSideDriveMotors, rightSideDriveMotors);

        leftEncoder = new Encoder(0,1);
        rightEncoder = new Encoder(2,3);

        leftSideDriveMotors.setInverted(true);
        rightSideDriveMotors.setInverted(true);

        // frontLeftEncoder = new CANEncoderWrapper(frontLeftMotor);
        // rearLeftEncoder = new CANEncoderWrapper(rearLeftMotor);
        // frontRightEncoder = new CANEncoderWrapper(frontRightMotor);
        // rearRightEncoder = new CANEncoderWrapper(rearRightMotor);

        try {
            navX = new AHRS(SPI.Port.kMXP);
        } catch (Exception e) {
            DriverStation.reportError("Error instantiating navX MXP:  " + e.getMessage(), true);
        }
    }

    @Override
    protected void initDefaultCommand() {
    }

    public void driveInit() {
        robotDrive.setSafetyEnabled(true);
        robotDrive.setExpiration(0.1);
        robotDrive.setMaxOutput(0.80);
        // leftEncoder.setDistancePerPulse(90);
        // rightEncoder.setDistancePerPulse(90);

    }

    /**
    * Drive function for external use
    * @param leftValue value for left motors
    * @param rightValue value right motors
    * @return void
    */
    public void tankDrive(double leftDrive, double rightDrive) {
        robotDrive.tankDrive(leftDrive, rightDrive);
    }

    public void arcadeDrive(double speed, double angle, boolean square){ 
        System.out.println("Speed " + speed);
        robotDrive.arcadeDrive(speed, angle, square);
    }

    public void encoderReset() {
        // frontLeftEncoder.reset();
        // rearLeftEncoder.reset();
        // frontRightEncoder.reset();
        // rearRightEncoder.reset();
        leftEncoder.reset();
        rightEncoder.reset();
        navX.reset();
    }

    public void encoderPrint() {
        Logging.logf("Average: %.2f Left encoder: %.2f Right encoder: %.2f NavX Angle: %.2f", getAverage(), getLeftEncoder(), getRightEncoder(), getNavX());
    }

    public double getLeftEncoder() {
        // return (frontLeftEncoder.get() * rearLeftEncoder.get()) / 2;
        return leftEncoder.getDistance();
    }

    public double getRightEncoder() {
        // return (frontRightEncoder.get() * rearRightEncoder.get()) / 2;
        return rightEncoder.getDistance();
    }

    public double getAverage() {
        // return (getRightEncoder() + getLeftEncoder()) / 2;
        return getLeftEncoder();
    }

    public double getNavX() {
        return navX.getAngle();
    }

}