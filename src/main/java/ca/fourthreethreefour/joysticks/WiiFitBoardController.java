package ca.fourthreethreefour.joysticks;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.GenericHID;

/**
 * Handle input from Nintendo Wii Fit Board controller connected to the Driver
 * Station through vJoy.
 *
 * <p>
 * This class handles Nintendo Wii Fit Board input that comes from the Driver
 * Station through vJoy. Each time a value is requested the most recent value is
 * returned. There is a single class instance for each controller and the
 * mapping of ports to hardware buttons depends on the code in the Driver
 * Station.
 * 
 * WARNING: Please note that there may be dangers with setting this up. I, nor my team,
 * are responsible for anything going wrong. Note that you do require a stable bluetooth
 * connection to make the board work, and for the correct values to be sent, and that
 * there comes the risk of something going wrong. PLEASE ENSURE THAT IF YOU DO THIS, THERE
 * IS ALWAYS SOMEONE AT STANDBY READY TO DISABLE THE ROBOT.
 * 
 * How to use the Wii Fit Board:
 * 
 * Instructions are for Windows. Likely will break for anything else.
 *
 *    1. Install the Headsoft VJoy Virtual Joystick Driver: http://www.headsoft.com.au/index.php?category=vjoy
 *    2. Install the Wii Balance Walker application: https://web.archive.org/web/20171012162643/http://www.greycube.com/site/download.php?view.68
 *    3. Install the included "Optional_Headsoft_VJoy_1.2 Driver"
 *    4. Connect the Wii Balance Board to the Wii Balance Walker application. Please leave this application open while driving as it runs better with it open. Make sure to turn "Enable Joystick" on and in the side "Actions" tab set everything to "Do Nothing"
 *    5. Once the Wii Balance Board is connected, open up VJoy application. Please note that the application itself is called VJoy and will open an instance. Go to the taskbar and right click the VJoy instance and click open. Ensure it's enabled.
 *    6. Check the driver station. You should see two new controllers appear. (Usually called something along the lines of: "* axis 32 button device..") The first one is the one that's connected to the Wii Fit Balance Board. This is kinda luck? It usually happens, but sometimes if it doesn't work the easiest thing is to uninstall and reinstall the VJoy stuff.
 *    7. If connected properly, in the axis section of the controller, the X and Y axis will shift as your weight shifts. Note if nothing is on there it should only be 0, but it can glitch out a bit. ONLY ENABLE ONCE SOMEONE IS ON THE BOARD, WEIGHT IS CENTERED (Done through the wii walker app) AND THE VALUES ARE SHOWING NORMAL STUFF.
 *    8. In the code, use this class (or any other way to grab the x and y axis) as your axis values.
 *
 * @author Cool J. Kornak. Team 4334
 * @since Whenever I had a lot of time on my hand. Or in this case 2019-03-14
 */
public class WiiFitBoardController extends GenericHID {

    /**
   * Construct an instance of a joystick. The joystick index is the USB port on the drivers
   * station.
   *
   * @param port The port on the Driver Station that the joystick is plugged into.
   */
    public WiiFitBoardController(final int port) {
        super(port);

        HAL.report(tResourceType.kResourceType_XboxController, port);
    }

    @Override
    public double getX(Hand hand) {
        return getRawAxis(0);
    }

    @Override
    public double getY(Hand hand) {
        return getRawAxis(1);
    }

}