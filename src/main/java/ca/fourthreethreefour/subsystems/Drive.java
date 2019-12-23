package ca.fourthreethreefour.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import ca.fourthreethreefour.commands.debug.Logging;
import ca.fourthreethreefour.wrapper.CANEncoderWrapper;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drive extends Subsystem {

    public CANSparkMax frontLeftMotor;
    public CANSparkMax rearLeftMotor;
    public CANSparkMax frontRightMotor;
    public CANSparkMax rearRightMotor;

    // Initialize the drivetrain motors
    // private WPI_TalonSRX leftDriveMotor = new WPI_TalonSRX(0);
    // private WPI_TalonSRX rightDriveMotor = new WPI_TalonSRX(1);

    // Pairs up the drivetrain motors based on their respective side and initializes the drivetrain controlling object
    private SpeedControllerGroup leftSideDriveMotors;
    private SpeedControllerGroup rightSideDriveMotors;
    private DifferentialDrive robotDrive;

    private CANEncoderWrapper frontLeftEncoder;
    private CANEncoderWrapper rearLeftEncoder;
    private CANEncoderWrapper frontRightEncoder;
    private CANEncoderWrapper rearRightEncoder;
    // Sets the appropriate configuration settings for the motors

    public Drive() {
        frontLeftMotor = new CANSparkMax(3, MotorType.kBrushless);
        rearLeftMotor = new CANSparkMax(4, MotorType.kBrushless);
        frontRightMotor = new CANSparkMax(1, MotorType.kBrushless);
        rearRightMotor = new CANSparkMax(2, MotorType.kBrushless);

        leftSideDriveMotors = new SpeedControllerGroup(frontLeftMotor, rearLeftMotor);
        rightSideDriveMotors = new SpeedControllerGroup(frontRightMotor, rearRightMotor);
        robotDrive = new DifferentialDrive(leftSideDriveMotors, rightSideDriveMotors);

        frontLeftEncoder = new CANEncoderWrapper(frontLeftMotor);
        rearLeftEncoder = new CANEncoderWrapper(rearLeftMotor);
        frontRightEncoder = new CANEncoderWrapper(frontRightMotor);
        rearRightEncoder = new CANEncoderWrapper(rearRightMotor);
    }

    @Override
    protected void initDefaultCommand() {
    }

    public void driveInit() {
        
        leftSideDriveMotors.setInverted(true);
        rightSideDriveMotors.setInverted(true);
        robotDrive.setSafetyEnabled(true);
        robotDrive.setExpiration(0.1);
        robotDrive.setMaxOutput(0.80);

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
        robotDrive.arcadeDrive(speed, angle, square);
    }

    public void encoderReset() {
        frontLeftEncoder.reset();
        rearLeftEncoder.reset();
        frontRightEncoder.reset();
        rearRightEncoder.reset();
        System.out.println("Hit");
    }

    public void encoderPrint() {
        Logging.logf("Average: %.2f Left encoder: %.2f Right encoder: %.2f", getAverage(), getLeftEncoder(), getRightEncoder());
    }

    public double getLeftEncoder() {
        return (frontLeftEncoder.get() * rearLeftEncoder.get()) / 2;
    }

    public double getRightEncoder() {
        return (frontRightEncoder.get() * rearRightEncoder.get()) / 2;
    }

    public double getAverage() {
        return (getRightEncoder() + getLeftEncoder()) / 2;
    }

}