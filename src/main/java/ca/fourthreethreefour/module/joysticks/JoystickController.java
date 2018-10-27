package ca.fourthreethreefour.module.joysticks;

import edu.first.module.joysticks.BindingJoystick;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The Joystick controller. Is bindable.
 *
 * <p>
 * Raw buttons:
 *
 * <pre>
 * </pre>
 *
 * @since October 27 18
 * @author Cool Kornak using code written by Joel Gallant
 */
public class JoystickController extends BindingJoystick {

    /**
     * Port for button.
     */
    public static final int TRIGGER = 1, STICK_DOWN = 2,
            STICK_UP = 3, STICK_LEFT = 4, STICK_RIGHT = 5,
            LEFT_UP = 6, LEFT_DOWN = 7, BOTTOM_LEFT = 8,
            BOTTOM_RIGHT = 9, RIGHT_DOWN = 10, RIGHT_UP = 11;
    /**
     * Port for axis.
     */
    public static final int X_AXIS = 0, Y_AXIS = 1, Z_AXIS = 2, FROM_MIDDLE = 3;
    
    
    /**
     * Constructs the joystick with the {@link edu.wpi.first.wpilibj.Joystick}
     * object that this joystick gets input from.
     *
     * @param joystick the composing instance to get input from
     */
    protected JoystickController(Joystick joystick) {
        super(joystick);

        increaseAxisCapacity(1);
        setAxis(FROM_MIDDLE, new FromMiddle(getXAxis(), getYAxis()));
        invertAxis(Z_AXIS);
    }

    /**
     * Constructs the joystick with the {@link edu.wpi.first.wpilibj.Joystick}
     * object that this joystick gets input from. Adds a deadband to every stick
     * input (X_AXIS, Y_AXIS).
     *
     * @param joystick the composing instance to get input from
     * @param stickDeadband threshold of minimum input for stick axises
     * @see #addDeadband(int, double)
     */
    protected JoystickController(Joystick joystick, double stickDeadband) {
        this(joystick);

        addDeadband(X_AXIS, stickDeadband);
        addDeadband(Y_AXIS, stickDeadband);
        addDeadband(FROM_MIDDLE, stickDeadband);
    }

    /**
     * Constructs the joystick with a port on the DriverStation.
     *
     * @param port channel in configuration of DriverStation
     */
    public JoystickController(int port) {
        this(new Joystick(port));
    }

    /**
     * Constructs the joystick with a port on the DriverStation. Adds a deadband
     * to every stick input (left X, left Y, right X, right Y).
     *
     * @param port channel in configuration of DriverStation
     * @param stickDeadband threshold of minimum input for stick axises
     * @see #addDeadband(int, double)
     */
    public JoystickController(int port, double stickDeadband) {
        this(new Joystick(port), stickDeadband);
    }

    /**
     * Returns the value of the X axis.
     *
     * <p>
     * Left: Negative; Right: Positive
     *
     * @return x axis value
     */
    public final double getXAxisValue() {
        return getRawAxisValue(X_AXIS);
    }

    /**
     * Returns an {@link Axis} that will give values from the axis. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * <p>
     * Left: Negative; Right: Positive
     *
     * @return axis object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Axis getXAxis() {
        return getRawAxis(X_AXIS);
    }

    
    /**
     * Returns the value of the Y axis.
     *
     * <p>
     * Up: Negative; Down: Positive
     *
     * @return x axis value
     */
    public final double getYAxisValue() {
        return getRawAxisValue(Y_AXIS);
    }

    /**
     * Returns an {@link Axis} that will give values from the axis. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * <p>
     * Up: Negative; Down: Positive
     *
     * @return axis object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Axis getYAxis() {
        return getRawAxis(Y_AXIS);
    }

    
    /**
     * Returns the value of the Z axis.
     *
     * <p>
     * Up: Positive; Down: Negative
     *
     * @return x axis value
     */
    public final double getZAxisValue() {
        return getRawAxisValue(Z_AXIS);
    }

    /**
     * Returns an {@link Axis} that will give values from the axis. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * <p>
     * Up: Positive; Down: Negative
     *
     * @return axis object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Axis getZAxis() {
        return getRawAxis(Z_AXIS);
    }

    /**
     * Returns the distance that the stick is relative to the absolute
     * centre.
     *
     * <p>
     * Up: Positive; Down: Negative
     *
     * @return  distance from middle
     */
    public final double getDistanceFromMiddleValue() {
        return getRawAxisValue(FROM_MIDDLE);
    }

    /**
     * Returns an {@link Axis} that will give values from the axis. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * <p>
     * Up: Positive; Down: Negative
     *
     * @return axis object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Axis getDistanceFromMiddle() {
        return getRawAxis(FROM_MIDDLE);
    }

    /**
     * Returns whether the Trigger button is pressed.
     *
     * @return if Trigger is pressed
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
     * Returns whether the Stick Up button is pressed.
     *
     * @return if Stick Up is pressed
     */
    public final boolean getStickUpValue() {
        return getRawButtonValue(STICK_UP);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getStickUp() {
        return getRawButton(STICK_UP);
    }
    
    /**
     * Returns whether the Stick Down button is pressed.
     *
     * @return if Stick Down is pressed
     */
    public final boolean getStickDownValue() {
        return getRawButtonValue(STICK_DOWN);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getStickDown() {
        return getRawButton(STICK_DOWN);
    }
    
    /**
     * Returns whether the Stick Left button is pressed.
     *
     * @return if Stick Left is pressed
     */
    public final boolean getStickLeftValue() {
        return getRawButtonValue(STICK_LEFT);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getStickLeft() {
        return getRawButton(STICK_LEFT);
    }
    
    /**
     * Returns whether the Stick Right button is pressed.
     *
     * @return if Stick Right is pressed
     */
    public final boolean getStickRightValue() {
        return getRawButtonValue(STICK_RIGHT);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getStickRight() {
        return getRawButton(STICK_RIGHT);
    }
    
    /**
     * Returns whether the Left Up button is pressed.
     *
     * @return if Left Up is pressed
     */
    public final boolean getLeftUpValue() {
        return getRawButtonValue(LEFT_UP);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getLeftUp() {
        return getRawButton(LEFT_UP);
    }
    
    /**
     * Returns whether the Left Down button is pressed.
     *
     * @return if Left Down is pressed
     */
    public final boolean getLeftDownValue() {
        return getRawButtonValue(LEFT_DOWN);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getLeftDown() {
        return getRawButton(LEFT_DOWN);
    }
    
    /**
     * Returns whether the Bottom Left button is pressed.
     *
     * @return if Bottom Left is pressed
     */
    public final boolean getBottomLeftValue() {
        return getRawButtonValue(BOTTOM_LEFT);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getBottomLeft() {
        return getRawButton(BOTTOM_LEFT);
    }
    
    /**
     * Returns whether the Bottom Right button is pressed.
     *
     * @return if Bottom Right is pressed
     */
    public final boolean getBottomRightValue() {
        return getRawButtonValue(BOTTOM_RIGHT);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getBottomRight() {
        return getRawButton(BOTTOM_RIGHT);
    }
    
    /**
     * Returns whether the Right Down button is pressed.
     *
     * @return if Right Down is pressed
     */
    public final boolean getRightDownValue() {
        return getRawButtonValue(RIGHT_DOWN);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getRightDown() {
        return getRawButton(RIGHT_DOWN);
    }
    
    /**
     * Returns whether the Right Up button is pressed.
     *
     * @return if Right Up is pressed
     */
    public final boolean getRightUpValue() {
        return getRawButtonValue(RIGHT_UP);
    }

    /**
     * Returns a {@link Button} that will give values from the button. Changed
     * settings of the controller will not affect this object after it has
     * already been created.
     *
     * @return button object to receive input from
     */
    public final edu.first.module.joysticks.Joystick.Button getRightUp() {
        return getRawButton(RIGHT_UP);
    }
    
    // Uses A^2 + B^2 = C^2 to solve distance from the middle of the joystick
    private static final class FromMiddle implements edu.first.module.joysticks.Joystick.Axis {

        private final edu.first.module.joysticks.Joystick.Axis X;
        private final edu.first.module.joysticks.Joystick.Axis Y;

        public FromMiddle(edu.first.module.joysticks.Joystick.Axis Y, edu.first.module.joysticks.Joystick.Axis X) {
            this.Y = Y;
            this.X = X;
        }

        @Override
        public double get() {
            double x = X.get();
            double y = Y.get();

            double distance = Math.sqrt((x * x) + (y * y));
            return (y > 0) ? distance : -distance;
        }
    }
}