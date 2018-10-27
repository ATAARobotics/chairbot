package ca.fourthreethreefour.module.joysticks;

import edu.first.module.joysticks.BindingJoystick;
import edu.first.module.joysticks.Joystick.Button;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The Nintendo Switch Right JoyCon controller. Is bindable.
 *
 * <p>
 * Raw buttons:
 *
 * <pre>
 * 0: A
 * 1: X
 * 2: B
 * 3: Y
 * 4: Left Bumper
 * 5: Right Bumper
 * 9: Plus
 * 11: Stick
 * 12: Home
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

public class JoyConRightController extends BindingJoystick {
    
    /**
     * Port for button.
     */
	public static final int A = 1, X = 2, B = 3, Y = 4, 
			LEFT_BUMPER = 5, RIGHT_BUMPER = 6, PLUS = 10, STICK = 12, 
            HOME = 13, BUMPER = 15, TRIGGER = 16, 
            STICK_UP = 17, STICK_DOWN = 18, STICK_LEFT = 19, STICK_RIGHT = 20;

	protected JoyConRightController(Joystick joystick) {
        super(joystick);
        
        setButton(STICK_UP, this.getPOVAsButton(90));
        setButton(STICK_DOWN, this.getPOVAsButton(270));
        setButton(STICK_LEFT, this.getPOVAsButton(0));
        setButton(STICK_RIGHT, this.getPOVAsButton(180));
	}

    /**
     * Constructs the joystick with a port on the DriverStation.
     *
     * @param port channel in configuration of DriverStation
     */
	public JoyConRightController(int port) {
		this(new Joystick(port));
	}

	/**
     * Returns whether the A button is pressed.
     *
     * @return if A is pressed
     */
    public final boolean getAValue() {
        return getRawButtonValue(A);
    }
	
    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getA() {
        return getRawButton(A);
    }
    

    /**
     * Returns whether the B button is pressed.
     *
     * @return if B is pressed
     */
    public final boolean getBValue() {
        return getRawButtonValue(B);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getB() {
        return getRawButton(B);
    }

    /**
     * Returns whether the X button is pressed.
     *
     * @return if X is pressed
     */
    public final boolean getXValue() {
        return getRawButtonValue(X);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getX() {
        return getRawButton(X);
    }

    /**
     * Returns whether the Y button is pressed.
     *
     * @return if Y is pressed
     */
    public final boolean getYValue() {
        return getRawButtonValue(Y);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getY() {
        return getRawButton(Y);
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
     * Returns whether the plus button is pressed.
     *
     * @return if plus is pressed
     */
    public final boolean getPlusValue() {
        return getRawButtonValue(PLUS);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getPlus() {
        return getRawButton(PLUS);
    }
    
    /**
     * Returns whether the home button is pressed.
     *
     * @return if home is pressed
     */
    public final boolean getHomeValue() {
        return getRawButtonValue(HOME);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getHome() {
        return getRawButton(HOME);
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
