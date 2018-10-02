package ca.fourthreethreefour.module.joysticks;

import edu.first.module.joysticks.BindingJoystick;
import edu.first.module.joysticks.Joystick.Button;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The Nintendo Switch Left JoyCon controller. Is bindable.
 *
 * <p>
 * Raw buttons:
 *
 * <pre>
 * 0: Left
 * 1: Down
 * 2: Up
 * 3: Right
 * 4: Left Bumper
 * 5: Right Bumper
 * 8: Minus
 * 10: Stick
 * 13: Capture
 * 14: Bumper
 * 15: Trigger
 * 
 * Note: This part is based on the controller is being held upwards.
 * 16: Stick_Up
 * 17: Stick_Down
 * 18: Stick_Left
 * 19: Stick_Right
 * </pre>
 *
 * @since October 2 18
 * @author Cool Kornak using code written by Joel Gallant
 */

public class JoyConLeftController extends BindingJoystick {
    
    /**
     * Port for button.
     */
	public static final int LEFT = 0, DOWN = 1, UP = 2, RIGHT = 3, 
			LEFT_BUMPER = 4, RIGHT_BUMPER = 5, MINUS = 8, STICK = 10, 
            CAPTURE = 13, BUMPER = 14, TRIGGER = 15, 
            STICK_UP = 16, STICK_DOWN = 17, STICK_LEFT = 18, STICK_RIGHT = 19;

	protected JoyConLeftController(Joystick joystick) {
        super(joystick);
        
        setButton(STICK_UP, this.getPOVAsButton(270));
        setButton(STICK_DOWN, this.getPOVAsButton(90));
        setButton(STICK_LEFT, this.getPOVAsButton(180));
        setButton(STICK_RIGHT, this.getPOVAsButton(0));
	}

    /**
     * Constructs the joystick with a port on the DriverStation.
     *
     * @param port channel in configuration of DriverStation
     */
	public JoyConLeftController(int port) {
		this(new Joystick(port));
	}

	/**
     * Returns whether the left button is pressed.
     *
     * @return if left is pressed
     */
    public final boolean getLeftValue() {
        return getRawButtonValue(LEFT);
    }
	
    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getLeft() {
        return getRawButton(LEFT);
    }
    

    /**
     * Returns whether the up button is pressed.
     *
     * @return if up is pressed
     */
    public final boolean getUpValue() {
        return getRawButtonValue(UP);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getUp() {
        return getRawButton(UP);
    }

    /**
     * Returns whether the down button is pressed.
     *
     * @return if down is pressed
     */
    public final boolean getDownValue() {
        return getRawButtonValue(DOWN);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getDown() {
        return getRawButton(DOWN);
    }

    /**
     * Returns whether the right button is pressed.
     *
     * @return if right is pressed
     */
    public final boolean getRightValue() {
        return getRawButtonValue(RIGHT);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getRight() {
        return getRawButton(RIGHT);
    }
    
    /**
     * Returns whether the left bumper button is pressed.
     *
     * @return if left bumper is pressed
     */
    public final boolean getLeftBumperValue() {
        return getRawButtonValue(LEFT_BUMPER);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getLeftBumper() {
        return getRawButton(LEFT_BUMPER);
    }

    /**
     * Returns whether the right bumper button is pressed.
     *
     * @return if right bumper is pressed
     */
    public final boolean getRightBumperValue() {
        return getRawButtonValue(RIGHT_BUMPER);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getRightBumper() {
        return getRawButton(RIGHT_BUMPER);
    }
    
    /**
     * Returns whether the bumper button is pressed.
     *
     * @return if bumper is pressed
     */
    public final boolean getBumperValue() {
        return getRawButtonValue(BUMPER);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getBumper() {
        return getRawButton(BUMPER);
    }
    
    /**
     * Returns whether the trigger button is pressed.
     *
     * @return if trigger is pressed
     */
    public final boolean getTriggerValue() {
        return getRawButtonValue(TRIGGER);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getTrigger() {
        return getRawButton(TRIGGER);
    }
    
    /**
     * Returns whether the minus button is pressed.
     *
     * @return if minus is pressed
     */
    public final boolean getMinusValue() {
        return getRawButtonValue(MINUS);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getMinus() {
        return getRawButton(MINUS);
    }
    
    /**
     * Returns whether the capture button is pressed.
     *
     * @return if capture is pressed
     */
    public final boolean getCaptureValue() {
        return getRawButtonValue(CAPTURE);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getCapture() {
        return getRawButton(CAPTURE);
    }
    
    /**
     * Returns whether the stick button is pressed.
     *
     * @return if stick is pressed
     */
    public final boolean getStickValue() {
        return getRawButtonValue(STICK);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getStick() {
        return getRawButton(STICK);
    }
}
