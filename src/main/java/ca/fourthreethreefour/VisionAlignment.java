package ca.fourthreethreefour;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.vision.VisionThread;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class VisionAlignment {

    static GripPipeline visionProcessing;
    // TODO remove debugging value
    final boolean debuggingEnabled = true;

    // Vision Proccessing
    private static final Scalar DEBUGGING_COLOR = new Scalar(0, 0, 255);
    private static final int IMG_WIDTH = 320;
    private static final int IMG_HEIGHT = 240;
    private VisionThread visionThread;
    private RotatedRect[] visionTarget;
    private Byte recievedInstructions;
    // TODO set values for where our targets should be after tetsing.
    private RotatedRect[] visionTargetReference = { new RotatedRect(new Point(2, 2), new Size(2, 2), 15.0),
            new RotatedRect(new Point(2, 2), new Size(2, 2), -15.0) };

    // TODO use below values when driver assit code is ready to be added
    private final Object imgLock = new Object();

    ShuffleboardTab dynamicSettingsTab = Shuffleboard.getTab("Dynamic Settings");
    /*
     * ShuffleboardTab portsTab = Shuffleboard.getTab("Ports"); ShuffleboardTab
     * outputTab = Shuffleboard.getTab("Output");
     */

    // TODO move to a shuffleboard options class
    // Create Item on Shuffleboard to Adjust Camera Exposure
    NetworkTableEntry CAMERAEXPOSURE_ENTRY = dynamicSettingsTab.addPersistent("Camera Exposure", 0).getEntry();
    int CAMERAEXPOSURE = (int) CAMERAEXPOSURE_ENTRY.getDouble(50);

    public VisionAlignment() {
        // Creates variable for storage of Rectangle Points
        Point[] boxPoints = new Point[4];

        // Initialize Camera with properties
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(IMG_WIDTH, IMG_HEIGHT);

        // Initializes image Mat for modification
        Mat currentFrame = new Mat();

        // Starts CvSink to capture Mats
        CvSink cvSink = CameraServer.getInstance().getVideo();

        // TODO remove once debugging is done
        CvSource outputStream = CameraServer.getInstance().putVideo("Image Analysis", IMG_WIDTH, IMG_HEIGHT);

        // Makes GripPipeline Object
        visionProcessing = new GripPipeline();

        visionThread = new VisionThread(camera, visionProcessing, pipeline -> {

            // Grabs frame for processing
            cvSink.grabFrame(currentFrame);

            // Initializes new Rect array to store data for assist code
            RotatedRect[] visionTarget = new RotatedRect[2];
            RotatedRect placeHolder = new RotatedRect();
            visionTarget[0] = placeHolder;
            visionTarget[1] = visionTarget[0];

            // TODO test if this is is necessary
            // Processes Image
            visionProcessing.process(currentFrame);

            // If filter has nothing, send frame
            if (pipeline.filterContoursOutput().size() < 2) {
                outputStream.putFrame(currentFrame);
                visionTarget = visionTargetReference;
            }

            // Sorts rectangles into visionTarget where [0] is largest and [1] is second
            // largest
            else {

                // Determines the two largest rectangles puts them in visionTarget
                for (int i = 0; i < pipeline.filterContoursOutput().size(); i++) {

                    MatOfPoint mop = pipeline.filterContoursOutput().get(i);
                    MatOfPoint2f mop2f = new MatOfPoint2f();

                    mop2f.fromArray(mop.toArray());
                    // Creates temporary object
                    RotatedRect currentRectangle = Imgproc.minAreaRect(mop2f);

                    // If the current rectangle is larger than our largest
                    if (currentRectangle.size.area() > visionTarget[0].size.area()) {

                        // Changes largest target to second largest
                        visionTarget[1] = visionTarget[0];

                        // Changes largest target to current target
                        visionTarget[0] = currentRectangle;
                    }

                    // If the current rectangle is larger thanm the second largest
                    else if (currentRectangle.size.area() > visionTarget[1].size.area()) {

                        // Changes second largest target to current rectangles
                        visionTarget[1] = currentRectangle;
                    }
                }
            }
            // TODO Remove below when done debugging
            // Draws rectangles
            if (debuggingEnabled) {
                visionTarget[0].points(boxPoints);
                for (int j = 0; j < 4; j++) {
                    Imgproc.line(currentFrame, boxPoints[j], boxPoints[(j + 1) % 4], DEBUGGING_COLOR, 3);
                }
                visionTarget[1].points(boxPoints);
                for (int j = 0; j < 4; j++) {
                    Imgproc.line(currentFrame, boxPoints[j], boxPoints[(j + 1) % 4], DEBUGGING_COLOR, 3);
                }
                // Send Processed frame
                outputStream.putFrame(currentFrame);
            }
            // Logging.put(CONTOURS_DETECTED_BOOLEAN, true);

            // Synchronizes data to prevent acess to variables
            synchronized (imgLock) {

            }
            align();
        });
        visionThread.start();
        try {
            visionThread.wait();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // TODO add something to do here
    private void align() {
        double centerX;
        synchronized (imgLock) {
            centerX = visionTarget[0].center.x;
        }
        // if (!globalPipeline.filterContoursOutput().isEmpty()){
        // double turn = 0;
        // turn = centerX - (IMG_WIDTH / 2);
        // System.out.println(turn);
        // }
        // double turn = centerX - (IMG_WIDTH / 2)
    }

    // TODO figure out data sending from pi to rio
    private void sendData() {
        // Scheme should be:
        // Bit 0: Boolean for inreverse function
        // Bit 1-2: Operation to perform
        // Bit 3-7: Value to use
    }

    // TODO Set up data parsing
    public void recieveData() {
        // Data scheme:
        // Bit 0: Boolean for hatch or panel
        // Bit 1: Boolean for recieve or output

        visionThread.notify();
    }
}