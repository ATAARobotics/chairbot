/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.fourthreethreefour;

import java.awt.Image;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.vision.VisionThread;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

// If you rename or move this class, update the build.properties file in the project root
public class Robot extends TimedRobot implements Constants
{
    // Initialize an Xbox 360 controller to control the robot
    private XboxController controller;
    
    // Initialize the drivetrain motors
    private WPI_TalonSRX gearMotor;
    private WPI_TalonSRX leftDriveMotor;
    // private WPI_TalonSRX rightDriveMotor1;
    private WPI_TalonSRX rightDriveMotor;
    
    // Pairs up the drivetrain motors based on their respective side and initializes
    // the drivetrain controlling object
    private SpeedControllerGroup leftSideDriveMotors;
    private SpeedControllerGroup rightSideDriveMotors;
    private DifferentialDrive robotDrive;
    
    // Vision Proccessing
    private static final int IMG_WIDTH = 320;
    private static final int IMG_HEIGHT = 240;
    private VisionThread visionThread;
    private double centerX = 0.0;
    Rect[] visionTarget = new Rect[2];

    
    //TODO use below values when driver assit code is ready to be added
    //private double centerX = 0.0;
    private List<Rect> rectList = new LinkedList<Rect>();
	private final Object imgLock = new Object();
    
    // Ultrasonic goes here
    
    
    ShuffleboardTab dynamicSettingsTab = Shuffleboard.getTab("Dynamic Settings");
    ShuffleboardTab portsTab = Shuffleboard.getTab("Ports");
    ShuffleboardTab outputTab = Shuffleboard.getTab("Output");
    
    NetworkTableEntry LOGGING_ENABLED_ENTRY = dynamicSettingsTab.addPersistent("Logging", false).withWidget(BuiltInWidgets.kToggleSwitch).getEntry();
    static public boolean LOGGING_ENABLED;
    
    NetworkTableEntry DRIVE_SPEED_ENTRY = dynamicSettingsTab.addPersistent("Drive Speed", 1).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", 1)).getEntry();
    double DRIVE_SPEED;
    NetworkTableEntry DRIVE_COMPENSATION_ENTRY = dynamicSettingsTab.addPersistent("Drive Compensation", 0).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", -0.7, "max", 0.7)).getEntry();
    double DRIVE_COMPENSATION;
    NetworkTableEntry TURN_CURVE_ENTRY = dynamicSettingsTab.addPersistent("Turn Curve", 1.5).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 1, "max", 10)).getEntry();
    double TURN_CURVE;
    
    
    
    NetworkTableEntry XBOXCONTROLLER_ENTRY = portsTab.addPersistent("XboxController", 0).getEntry();
    int XBOXCONTROLLER = (int) XBOXCONTROLLER_ENTRY.getDouble(0);
    NetworkTableEntry GEAR_MOTOR_ENTRY = portsTab.addPersistent("Gear Motor", 2).getEntry();
    int GEAR_MOTOR = (int) GEAR_MOTOR_ENTRY.getDouble(2);
    NetworkTableEntry LEFT_DRIVE_MOTOR_ENTRY = portsTab.addPersistent("Left Drive Motor", 1).getEntry();
    int LEFT_DRIVE_MOTOR = (int) LEFT_DRIVE_MOTOR_ENTRY.getDouble(1);
    NetworkTableEntry RIGHT_DRIVE_MOTOR_ENTRY = portsTab.addPersistent("Right Drive Motor", 3).getEntry();
    int RIGHT_DRIVE_MOTOR = (int) RIGHT_DRIVE_MOTOR_ENTRY.getDouble(3);
    
    //Create Item on Shuffleboard to Adjust Camera Exposure
    NetworkTableEntry CAMERAEXPOSURE_ENTRY = dynamicSettingsTab.addPersistent("Camera Exposure", 0).getEntry();
    int CAMERAEXPOSURE = (int) CAMERAEXPOSURE_ENTRY.getDouble(50);
    
    @Override
    public void robotInit()
    {
        // Assigns all the motors to their respective objects (the number in brackets is the port # of what is connected where)
        controller = new XboxController(XBOXCONTROLLER);
        
        gearMotor = new WPI_TalonSRX(GEAR_MOTOR);
        leftDriveMotor = new WPI_TalonSRX(LEFT_DRIVE_MOTOR);
        //rightDriveMotor1 = new WPI_TalonSRX(2);
        rightDriveMotor = new WPI_TalonSRX(RIGHT_DRIVE_MOTOR);
        
        // Assigns the drivetrain motors to their respective motor controller group and then passes them on to the drivetrain controller object
        leftSideDriveMotors = new SpeedControllerGroup(leftDriveMotor);
        rightSideDriveMotors = new SpeedControllerGroup(rightDriveMotor);
        robotDrive = new DifferentialDrive(leftSideDriveMotors, rightSideDriveMotors);
        
        // Initialize Camera with properties
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
        
        //Initializes image Mat for modification
        Mat source = new Mat();
        
        //Starts CvSink to capture Mats
        CvSink cvSink = CameraServer.getInstance().getVideo();
        
        //TODO remove once debugging is done
        CvSource outputStream = CameraServer.getInstance().putVideo("Image Analysis", IMG_WIDTH, IMG_HEIGHT);
        
        //Makes GripPipeline Object
        GripPipeline visionProcessing = new GripPipeline();
        
        
        //Configures vision Thread
        visionThread = new VisionThread(camera, visionProcessing, pipeline -> {

            //Sets Camera Exposure with value from Shuffleboard
            CAMERAEXPOSURE = (int) CAMERAEXPOSURE_ENTRY.getDouble(50);
            //System.out.println(CAMERAEXPOSURE);
            camera.setExposureManual(CAMERAEXPOSURE);

            //Grabs frame for processing
            cvSink.grabFrame(source);
            
            //Initializes new Rect array to store data for assist code
            Rect placeHolder = new Rect(0, 0, 0, 0);
            visionTarget[0] = placeHolder;
            visionTarget[1] = visionTarget[0];

            //TODO test if this is is necessary
            //Processes Image
            visionProcessing.process(source);

            //If filter has nothing, send frame
            if (!pipeline.filterContoursOutput().isEmpty()) {
                //Grabs frame for processing
                cvSink.grabFrame(source);
                //TODO try catch statement is commented out. This will not be removed until tests prove it's not used
                //try{
                    //Processes Image
                    visionProcessing.process(source);

                    //Creates All Rectangles
                    for(int i = 0; i < pipeline.filterContoursOutput().size(); i++){
                        Rect rx = Imgproc.boundingRect(pipeline.filterContoursOutput().get(i));

                        //Prints Location of Rectangle
                        System.out.println("Object " + i + ": " + rx.toString());

                        //Adds rectangle to List to store size and position values
                        rectList.add(rx);
                    }
                    //Sets up Indices For rectangle index holding
                    int largestIndex = 0;
                    int secondIndex = 0;
                    
                    if(rectList.size() == 2){
                        visionTarget[0] = rectList.get(0);
                        visionTarget[1] = rectList.get(1);
                    }
                    //If there is only one target, make our Vision target two of our same rectangle
                    if(rectList.size() == 1){
                        visionTarget[0] = rectList.get(0);
                        visionTarget[1] = visionTarget[0];
                    }
                    //Removes Rectangles from List only if there's more than two rectangles
                    else if(rectList.size() > 2){
                    //Determines the two largest rectangles and indexes them
                    for (int i = 1; i < rectList.size();i++){
                        //If the current rectangle is larger than our largest
                        if(rectList.get(largestIndex).area()<rectList.get(i).area()){
                            visionTarget[1] = visionTarget[0];
                            secondIndex = largestIndex;
                            visionTarget[0] = rectList.get(i);
                            largestIndex = i;
                        }
                        //If the current rectangle is larger thanm the second largest
                        else if(rectList.get(secondIndex).area()<rectList.get(i).area()){
                            visionTarget[1] = rectList.get(i);
                            secondIndex = i;
                        }
                    }
                    //Remove rectangles except for ones at our indexes
                    /*for(int i = 0; i < rectList.size();i++){
                        //If index tries to find third Largest rectangle, break
                        if(i == 2){
                            break;
                        }
                        //if our index hits one of our targets, draw the target
                        if(i == largestIndex ||i == secondIndex){
                            //Draws rectangle if it matches with either index
                            Imgproc.rectangle(source, new Point(rectList.get(i).x, rectList.get(i).y), new Point(rectList.get(i).x + rectList.get(i).width, rectList.get(i).y + rectList.get(i).height), new Scalar(0,0,255), 2);
                        }
                        //if all checks fail,
                        else{
                            //Finds rectangle
                            Rect r = rectList.get(i);
                            //Removes Rectangle from List
                            rectList.remove(r);
                            //Decrements all of our counters
                            largestIndex--;
                            secondIndex--;
                            i--;
                        }
                    }*/
                }
                //TODO Remove below when done debugging

                //Draws rectangles
                Imgproc.rectangle(source, new Point(visionTarget[0].x, visionTarget[0].y), new Point(visionTarget[0].x + visionTarget[0].width, visionTarget[0].y + visionTarget[0].height), new Scalar(0,0,255), 2);
                Imgproc.rectangle(source, new Point(visionTarget[1].x, visionTarget[1].y), new Point(visionTarget[1].x + visionTarget[1].width, visionTarget[1].y + visionTarget[1].height), new Scalar(0,0,255), 2);

                //Send Frame
                outputStream.putFrame(source);
                
                //Sends data
                synchronized(imgLock){
                    
                }

                //Clear list
                rectList.clear();

            //} 
            /*catch (IndexOutOfBoundsException | NullPointerException e){
                    //failsafe
                    System.out.println("No vision target detected " + e.getMessage());
                    outputStream.putFrame(source);
                }*/
            } else {
                outputStream.putFrame(source);
                System.out.println("No Contours Detected");
                visionTarget[0] = new Rect(0,0,0,0);
                visionTarget[1] = new Rect(0,0,0,0);
            }
            //TODO Remove below when done debugging
            //Draws rectangles
            Imgproc.rectangle(source, new Point(visionTarget[0].x, visionTarget[0].y), new Point(visionTarget[0].x + visionTarget[0].width, visionTarget[0].y + visionTarget[0].height), new Scalar(0,0,255), 2);
            Imgproc.rectangle(source, new Point(visionTarget[1].x, visionTarget[1].y), new Point(visionTarget[1].x + visionTarget[1].width, visionTarget[1].y + visionTarget[1].height), new Scalar(0,0,255), 2);
            //Send Frame
            outputStream.putFrame(source);
            
            //Sends data
            synchronized(imgLock){
                
            }
        });
        
        //starts vision thread
        visionThread.start(); 
        
        //drive = new RobotDrive(1, 2);
        
        // Sets the appropriate configuration settings for the motors
        leftSideDriveMotors.setInverted(true);
        rightSideDriveMotors.setInverted(true);
        robotDrive.setSafetyEnabled(true);
        robotDrive.setExpiration(0.1);
        robotDrive.setMaxOutput(0.80);
        gearMotor.setSafetyEnabled(true);
    }
    
    @Override
    public void autonomousInit()
    {
        // Enables motor safety for the drivetrain for teleop
        robotDrive.setSafetyEnabled(true);
        //gearMotor.setSafetyEnabled(true);
    }
    
    @Override
    public void autonomousPeriodic()
    {
        //Driver Assist with Vision - Auto Line Up with Single Reflector:
        double centerX;
        synchronized (imgLock) {
            //Get position of single target
            centerX = visionTarget[0].x + (visionTarget[0].width / 2);
        }
        if (visionTarget[0] == null){
            return;
        } else {
            double turn = 0;
            //Calculate Turn
            turn = centerX - (IMG_WIDTH / 2);
            System.out.println(turn);
            //Move Robot
            robotDrive.arcadeDrive(-0.6, turn * 0.005);
            try {
                //3 Second Time Delay
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                System.out.println("The Delay Failed.... This is problematic...");
            }
        }
        //double turn = centerX - (IMG_WIDTH / 2);
       
    }
    
    @Override
    public void teleopPeriodic()
    {
        double speed = controller.getY(GenericHID.Hand.kLeft);
        double turn = controller.getX(GenericHID.Hand.kRight);
        turn += (speed > 0) ? DRIVE_COMPENSATION : (speed < 0) ? -DRIVE_COMPENSATION : 0;
        // Sends the Y axis input from the left stick (speed) and the X axis input from the right stick (rotation) from the primary controller to move the robot
        robotDrive.arcadeDrive(speed * DRIVE_SPEED, turn >= 0 ? Math.pow(turn, TURN_CURVE) : -Math.pow(Math.abs(turn), TURN_CURVE));
        gearMotor.set(-controller.getTriggerAxis(GenericHID.Hand.kRight));
    }
    
    @Override
    public void disabledPeriodic() {
        updateSettings();
    }
    
    @Override
    public void testPeriodic(){
        
    }
    
    public void updateSettings() {
        LOGGING_ENABLED = LOGGING_ENABLED_ENTRY.getBoolean(false);
        
        DRIVE_SPEED = DRIVE_SPEED_ENTRY.getDouble(1);
        DRIVE_COMPENSATION = DRIVE_COMPENSATION_ENTRY.getDouble(0);
        TURN_CURVE = TURN_CURVE_ENTRY.getDouble(1.5);
    }

}
