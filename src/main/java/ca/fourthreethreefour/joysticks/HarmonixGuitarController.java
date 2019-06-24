package ca.fourthreethreefour.joysticks;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.GenericHID;

/**
 * Handle input from Harmonix Guitar controller connected to the Driver Station.
 *
 * <p>This class handles input that comes from the Driver Station. Each time a value is
 * requested the most recent value is returned. There is a single class instance for each controller
 * and the mapping of ports to hardware buttons depends on the code in the Driver Station.
 * 
 * @author Cool J. Kornak
 * @since Whenever I had a lot of time on my hand. Or in this case 2019-04-14
 */

 public class HarmonixGuitarController extends GenericHID {

    /**
     * Represents a digital button on an XboxController.
     */
    private enum Button {
        kGreen(2),
        kRed(3),
        kYellow(4),
        kBlue(1),
        kOrange(5),
        kBottom(7),
        kStart(10),
        kSelect(9);

        @SuppressWarnings({"MemberName", "PMD.SingularField"})
        private final int value;

        Button(int value) {
            this.value = value;
        }
    }

    /**
     * Construct an instance of a joystick. The joystick index is the USB port on the drivers
     * station.
     *
     * @param port The port on the Driver Station that the joystick is plugged into.
     */
    public HarmonixGuitarController(final int port) {
        super(port);

        HAL.report(tResourceType.kResourceType_XboxController, port);
    }

    /**
     * Get the X axis value of the controller.
     *
     * @return The X axis value of the controller.
     */
    @Override
    public double getX(Hand hand) {
        return 0;
    }

    /**
     * Get the Y axis value of the controller.
     *
     * @return The Y axis value of the controller.
     */
    @Override
    public double getY(Hand hand) {
        return 0;
    }

    /**
     * Get the Z axis value of the controller.
     *
     * @return The Z value of the controller.
     */
    public double getZ() {
        double value = 0;
        if (getRawAxis(2) != 0) {
            value = getRawAxis(2);
        }
        return value;
    }

    /**
     * Get the Z Rotate axis value of the controller.
     *
     * @return The Z Rotate value of the controller.
     */
    public double getZRotate() {
        double value = 0;
        if (getRawAxis(3) != 0) {
            value = getRawAxis(3);
        }
        return value;
    }

    /**
     * Read the value of the top Green button on the controller.
     * 
     * @return The state of the button.
     */
    public boolean getTopGreenButton() {
        boolean result = getRawButton(Button.kGreen.value) && !getRawButton(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the top Green button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getTopGreenButtonPressed() {
        boolean result = getRawButtonPressed(Button.kGreen.value) && !getRawButtonPressed(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the top Green button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getTopGreenButtonReleased() {
        boolean result = getRawButtonReleased(Button.kGreen.value) && !getRawButtonReleased(Button.kBottom.value);
        return result;
    }

    /**
     * Read the value of the bottom Green button on the controller.
     * 
     * @return The state of the button.
     */
    public boolean getBottomGreenButton() {
        boolean result = getRawButton(Button.kGreen.value) && getRawButton(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the bottom Green button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getBottomGreenButtonPressed() {
        boolean result = getRawButtonPressed(Button.kGreen.value) && getRawButtonPressed(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the bottom Green button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getBottomGreenButtonReleased() {
        boolean result = getRawButtonReleased(Button.kGreen.value) && getRawButtonReleased(Button.kBottom.value);
        return result;
    }

    /**
     * Read the value of the top Red button on the controller.
     * 
     * @return The state of the button.
     */
    public boolean getTopRedButton() {
        boolean result = getRawButton(Button.kRed.value) && !getRawButton(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the top Red button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getTopRedButtonPressed() {
        boolean result = getRawButtonPressed(Button.kRed.value) && !getRawButtonPressed(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the top Red button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getTopRedButtonReleased() {
        boolean result = getRawButtonReleased(Button.kRed.value) && !getRawButtonReleased(Button.kBottom.value);
        return result;
    }

    /**
     * Read the value of the bottom Red button on the controller.
     * 
     * @return The state of the button.
     */
    public boolean getBottomRedButton() {
        boolean result = getRawButton(Button.kRed.value) && getRawButton(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the bottom Red button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getBottomRedButtonPressed() {
        boolean result = getRawButtonPressed(Button.kRed.value) && getRawButtonPressed(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the bottom Red button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getBottomRedButtonReleased() {
        boolean result = getRawButtonReleased(Button.kRed.value) && getRawButtonReleased(Button.kBottom.value);
        return result;
    }

    /**
     * Read the value of the top Yellow button on the controller.
     * 
     * @return The state of the button.
     */
    public boolean getTopYellowButton() {
        boolean result = getRawButton(Button.kYellow.value) && !getRawButton(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the top Yellow button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getTopYellowButtonPressed() {
        boolean result = getRawButtonPressed(Button.kYellow.value) && !getRawButtonPressed(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the top Yellow button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getTopYellowButtonReleased() {
        boolean result = getRawButtonReleased(Button.kYellow.value) && !getRawButtonReleased(Button.kBottom.value);
        return result;
    }

    /**
     * Read the value of the bottom Yellow button on the controller.
     * 
     * @return The state of the button.
     */
    public boolean getBottomYellowButton() {
        boolean result = getRawButton(Button.kYellow.value) && getRawButton(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the bottom Yellow button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getBottomYellowButtonPressed() {
        boolean result = getRawButtonPressed(Button.kYellow.value) && getRawButtonPressed(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the bottom Yellow button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getBottomYellowButtonReleased() {
        boolean result = getRawButtonReleased(Button.kYellow.value) && getRawButtonReleased(Button.kBottom.value);
        return result;
    }

    /**
     * Read the value of the top Blue button on the controller.
     * 
     * @return The state of the button.
     */
    public boolean getTopBlueButton() {
        boolean result = getRawButton(Button.kBlue.value) && !getRawButton(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the top Blue button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getTopBlueButtonPressed() {
        boolean result = getRawButtonPressed(Button.kBlue.value) && !getRawButtonPressed(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the top Blue button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getTopBlueButtonReleased() {
        boolean result = getRawButtonReleased(Button.kBlue.value) && !getRawButtonReleased(Button.kBottom.value);
        return result;
    }

    /**
     * Read the value of the bottom Blue button on the controller.
     * 
     * @return The state of the button.
     */
    public boolean getBottomBlueButton() {
        boolean result = getRawButton(Button.kBlue.value) && getRawButton(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the bottom Blue button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getBottomBlueButtonPressed() {
        boolean result = getRawButtonPressed(Button.kBlue.value) && getRawButtonPressed(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the bottom Blue button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getBottomBlueButtonReleased() {
        boolean result = getRawButtonReleased(Button.kBlue.value) && getRawButtonReleased(Button.kBottom.value);
        return result;
    }

    /**
     * Read the value of the top Orange button on the controller.
     * 
     * @return The state of the button.
     */
    public boolean getTopOrangeButton() {
        boolean result = getRawButton(Button.kOrange.value) && !getRawButton(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the top Orange button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getTopOrangeButtonPressed() {
        boolean result = getRawButtonPressed(Button.kOrange.value) && !getRawButtonPressed(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the top Orange button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getTopOrangeButtonReleased() {
        boolean result = getRawButtonReleased(Button.kOrange.value) && !getRawButtonReleased(Button.kBottom.value);
        return result;
    }

    /**
     * Read the value of the bottom Orange button on the controller.
     * 
     * @return The state of the button.
     */
    public boolean getBottomOrangeButton() {
        boolean result = getRawButton(Button.kOrange.value) && getRawButton(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the bottom Orange button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getBottomOrangeButtonPressed() {
        boolean result = getRawButtonPressed(Button.kOrange.value) && getRawButtonPressed(Button.kBottom.value);
        return result;
    }

    /**
     * Whether the bottom Orange button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getBottomOrangeButtonReleased() {
        boolean result = getRawButtonReleased(Button.kOrange.value) && getRawButtonReleased(Button.kBottom.value);
        return result;
    }

    /**
     * Read the value of the Start button on the controller.
     * 
     * @return The state of the button.
     */
    public boolean getStartButton() {
        return getRawButton(Button.kStart.value);
    }

    /**
     * Whether the Start button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getStartButtonPressed() {
        return getRawButtonPressed(Button.kStart.value);
    }

    /**
     * Whether the Start button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getStartButtonReleased() {
        return getRawButtonReleased(Button.kStart.value);
    }

    /**
     * Read the value of the Select button on the controller.
     * 
     * @return The state of the button.
     */
    public boolean getSelectButton() {
        return getRawButton(Button.kSelect.value);
    }

    /**
     * Whether the Select button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getSelectButtonPressed() {
        return getRawButtonPressed(Button.kSelect.value);
    }

    /**
     * Whether the Select button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getSelectButtonReleased() {
        return getRawButtonReleased(Button.kSelect.value);
    }

 }