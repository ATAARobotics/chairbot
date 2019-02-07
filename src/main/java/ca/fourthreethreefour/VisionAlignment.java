package ca.fourthreethreefour;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class VisionAlignment{

     // Vision Proccessing
     private static final int IMG_WIDTH = 320;
     private static final int IMG_HEIGHT = 240;
     private VisionThread visionThread;
     private double centerX = 0.0;
     Rect[] visionTarget = new Rect[2];


     private DifferentialDrive robotDrive;
 
     
     //TODO use below values when driver assit code is ready to be added
     private List<Rect> rectList = new LinkedList<Rect>();
     private final Object imgLock = new Object();

     ShuffleboardTab dynamicSettingsTab = Shuffleboard.getTab("Dynamic Settings");
     ShuffleboardTab portsTab = Shuffleboard.getTab("Ports");
     ShuffleboardTab outputTab = Shuffleboard.getTab("Output");

     //LED_Relay Control
    NetworkTableEntry LEDRELAY_ENTRY = dynamicSettingsTab.addPersistent("Led Relay", true).getEntry();
    boolean LEDRELAY = LEDRELAY_ENTRY.getBoolean(true);
    Relay ledRelay = new Relay(0);

      //Create Item on Shuffleboard to Adjust Camera Exposure
    NetworkTableEntry CAMERAEXPOSURE_ENTRY = dynamicSettingsTab.addPersistent("Camera Exposure", 0).getEntry();
    int CAMERAEXPOSURE = (int) CAMERAEXPOSURE_ENTRY.getDouble(50);

    public VisionAlignment(SpeedControllerGroup leftDriveMotors, SpeedControllerGroup rightDriveMotors){

    
        robotDrive = new DifferentialDrive(leftDriveMotors, rightDriveMotors);

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
            Rect placeHolder = new Rect(0, 0, 1, 1);
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
 
    } public void align(){
        ledRelay.set(Value.kOn);
        double centerX;
        synchronized (imgLock) {
            centerX = visionTarget[0].x + (visionTarget[0].width / 2);
        }
        if (visionTarget[0] == null){
            return;
        }else {
            double turn = 0;
            turn = centerX - (IMG_WIDTH / 2);
            System.out.println(turn);
            robotDrive.arcadeDrive(-0.6, turn * 0.005);
        }
        //double turn = centerX - (IMG_WIDTH / 2)
    }
}
