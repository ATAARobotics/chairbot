package ca.fourthreethreefour;

import java.util.LinkedList;
import java.util.List;


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
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class VisionAlignment{

    GripPipeline globalPipeline;

     // Vision Proccessing
     private static final int IMG_WIDTH = 320;
     private static final int IMG_HEIGHT = 240;
     private static final int FOV = 60;
     private VisionThread visionThread;
     Rect[] visionTarget = new Rect[2];


     private DifferentialDrive robotDrive;
     //TODO use below values when driver assit code is ready to be added
     private List<Rect> rectList = new LinkedList<Rect>();
     private final Object imgLock = new Object();

     ShuffleboardTab dynamicSettingsTab = Shuffleboard.getTab("Dynamic Settings");
     ShuffleboardTab portsTab = Shuffleboard.getTab("Ports");
     ShuffleboardTab outputTab = Shuffleboard.getTab("Output");


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
            globalPipeline = pipeline;
            if (!pipeline.filterContoursOutput().isEmpty()) {
                //Grabs frame for processing
                cvSink.grabFrame(source);

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
 
    } public void align(Relay toggleSwitch){
        toggleSwitch.set(Value.kForward);
        double centerX;
        double focalLength;
        double angleToTarget;
        synchronized (imgLock) {
            //Calculates centerX
            centerX = visionTarget[0].width/2;
            //Calculates focalLength of camera
            focalLength = IMG_WIDTH/(2*Math.tan(FOV/2));

        }
        if (!globalPipeline.filterContoursOutput().isEmpty()){ 
            //Calculates the angle to the target
            angleToTarget = Math.atan((centerX - 159.5) / focalLength);

            /*TODO: Use encoder values to turn with the angle
            We can use encoder.getSelectedSensorPosition and divide by 3 000 000? to get cm
            Then we can use tell the encoder to move x cm depending on the angle
            (This will require a lot of testing and trial/error)
            The ideal is making angleToTarget equal to 0 
            (Note: Above calculations are not tested and I may have misinterpreted how to do them)
            */

            
        }
    }
}
