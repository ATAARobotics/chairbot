package ca.fourthreethreefour.joysticks;

import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.hal.HAL;

/**
 * Handle input from Nintendo Switch Joycon Left controller connected to the Driver Station.
 *
 * <p>This class handles Nintendo Switch input that comes from the Driver Station. Each time a value is
 * requested the most recent value is returned. There is a single class instance for each controller
 * and the mapping of ports to hardware buttons depends on the code in the Driver Station.
 * 
 * <p>Please note that the stick on these controllers' are the POV and not an axis. You'll have to do getPOV
 * to use the stick directions, so good luck. I may add easier support to here later? That's later.
 * 
 * @author Cool J. Kornak
 * @since Whenever I had a lot of time on my hand. Or in this case 2019-01-26
 */
public class JoyconLeftController extends GenericHID {
  /**
   * Represents a digital button on an XboxController.
   */
  private enum Button {
    kLeft(1),
    kDown(2),
    kUp(3),
    kRight(4),
    kBumperLeft(5),
    kBumperRight(6),
    kMinus(9),
    kStick(11),
    kCapture(14),
    kBumper(15),
    kTrigger(16);

    @SuppressWarnings({"MemberName", "PMD.SingularField"})
    private final int value;

    Button(int value) {
      this.value = value;
    }
  }

  /**
   * Which orientation the Joycon is in.
   */
  public enum Position {
    kHorizontal(0), kVertical(1);

    @SuppressWarnings("MemberName")
    public final int value;

    Position(int value) {
      this.value = value;
    }
  }

  /**
   * Which directional button being specified.
   */
  public enum Direction {
    kUp(0), kDown(1), kLeft(2), kRight(3);

    @SuppressWarnings("MemberName")
    public final int value;

    Direction(int value) {
      this.value = value;
    }
  }

  /**
   * Construct an instance of a joystick. The joystick index is the USB port on the drivers
   * station.
   *
   * @param port The port on the Driver Station that the joystick is plugged into.
   */
  public JoyconLeftController(final int port) {
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
      return getRawAxis(0);
  }

  /**
   * Get the Y axis value of the controller.
   *
   * @return The Y axis value of the controller.
   */
  @Override
  public double getY(Hand hand) {
    return getRawAxis(1);
  }

  /**
   * Get the X Rotate axis value of the controller.
   *
   * @return The X Rotate value of the controller.
   */
  public double getXRotate() {
    return getRawAxis(2);
  }

  /**
   * Get the Y Rotate axis value of the controller.
   *
   * @return The Y Rotate value of the controller.
   */
  public double getYRotate() {
    return getRawAxis(3);
  }

  /**
   * Read the value of the bumper button on the controller.
   *
   * @param hand Side of controller whose value should be returned.
   * @return The state of the button.
   */
  public boolean getBumper(Hand hand) {
    if (hand.equals(Hand.kLeft)) {
      return getRawButton(Button.kBumperLeft.value);
    } else {
      return getRawButton(Button.kBumperRight.value);
    }
  }

  /**
   * Whether the bumper was pressed since the last check.
   *
   * @param hand Side of controller whose value should be returned.
   * @return Whether the button was pressed since the last check.
   */
  public boolean getBumperPressed(Hand hand) {
    if (hand == Hand.kLeft) {
      return getRawButtonPressed(Button.kBumperLeft.value);
    } else {
      return getRawButtonPressed(Button.kBumperRight.value);
    }
  }

  /**
   * Whether the bumper was released since the last check.
   *
   * @param hand Side of controller whose value should be returned.
   * @return Whether the button was released since the last check.
   */
  public boolean getBumperReleased(Hand hand) {
    if (hand == Hand.kLeft) {
      return getRawButtonReleased(Button.kBumperLeft.value);
    } else {
      return getRawButtonReleased(Button.kBumperRight.value);
    }
  }

  /**
   * Read the value of the stick button on the controller.
   *
   * @return The state of the button.
   */
  public boolean getStickButton() {
    return getRawButton(Button.kStick.value);
  }

  /**
   * Whether the stick button was pressed since the last check.
   *
   * @return Whether the button was pressed since the last check.
   */
  public boolean getStickButtonPressed() {
    return getRawButtonPressed(Button.kStick.value);
  }

  /**
   * Whether the stick button was released since the last check.
   *
   * @return Whether the button was released since the last check.
   */
  public boolean getStickButtonReleased() {
    return getRawButtonReleased(Button.kStick.value);
  }

  
  /**
   * Read the value of the specified button on the controller.
   *
   * @param position Which orientation the controller is in. Horizonal or Vertical.
   * @param direction Which button in the orientation to press. Up Down Left or Right.
   * @return The state of the button. If no button is properly specified, returns false.
   */
  public boolean getDirectionalButton(Position position, Direction direction) {
    if (position == Position.kVertical) {
      switch(direction) {
        case kUp: 
          return getRawButton(Button.kUp.value);
        case kDown:
          return getRawButton(Button.kDown.value);
        case kLeft:
          return getRawButton(Button.kLeft.value);
        case kRight:
          return getRawButton(Button.kRight.value);
        default:
          return false;
      }
    } else {
      switch(direction) {
        case kUp: 
          return getRawButton(Button.kRight.value);
        case kDown:
          return getRawButton(Button.kLeft.value);
        case kLeft:
          return getRawButton(Button.kUp.value);
        case kRight:
          return getRawButton(Button.kDown.value);
        default:
          return false;
      }
    }
  }

  /**
   * Whether the specified button was pressed since the last check.
   *
   * @param position Which orientation the controller is in. Horizonal or Vertical.
   * @param direction Which button in the orientation to press. Up Down Left or Right.
   * @return Whether the button was pressed since the last check. If no button is properly specified, returns false.
   */
  public boolean getDirectionalButtonPressed(Position position, Direction direction) {
    if (position == Position.kVertical) {
      switch(direction) {
        case kUp: 
          return getRawButtonPressed(Button.kUp.value);
        case kDown:
          return getRawButtonPressed(Button.kDown.value);
        case kLeft:
          return getRawButtonPressed(Button.kLeft.value);
        case kRight:
          return getRawButtonPressed(Button.kRight.value);
        default:
          return false;
      }
    } else {
      switch(direction) {
        case kUp: 
          return getRawButtonPressed(Button.kRight.value);
        case kDown:
          return getRawButtonPressed(Button.kLeft.value);
        case kLeft:
          return getRawButtonPressed(Button.kUp.value);
        case kRight:
          return getRawButtonPressed(Button.kDown.value);
        default:
          return false;
      }
    }
  }

  /**
   * Whether the specified button was released since the last check.
   *
   * @param position Which orientation the controller is in. Horizonal or Vertical.
   * @param direction Which button in the orientation to press. Up Down Left or Right.
   * @return Whether the button was released since the last check. If no button is properly specified, returns false.
   */
  public boolean getDirectionalButtonReleased(Position position, Direction direction) {
    if (position == Position.kVertical) {
      switch(direction) {
        case kUp: 
          return getRawButtonReleased(Button.kUp.value);
        case kDown:
          return getRawButtonReleased(Button.kDown.value);
        case kLeft:
          return getRawButtonReleased(Button.kLeft.value);
        case kRight:
          return getRawButtonReleased(Button.kRight.value);
        default:
          return false;
      }
    } else {
      switch(direction) {
        case kUp: 
          return getRawButtonReleased(Button.kRight.value);
        case kDown:
          return getRawButtonReleased(Button.kLeft.value);
        case kLeft:
          return getRawButtonReleased(Button.kUp.value);
        case kRight:
          return getRawButtonReleased(Button.kDown.value);
        default:
          return false;
      }
    }
  }

  /**
   * Read the value of the capture button on the controller.
   *
   * @return The state of the button.
   */
  public boolean getCaptureButton() {
    return getRawButton(Button.kCapture.value);
  }

  /**
   * Whether the capture button was pressed since the last check.
   *
   * @return Whether the button was pressed since the last check.
   */
  public boolean getCaptureButtonPressed() {
    return getRawButtonPressed(Button.kCapture.value);
  }

  /**
   * Whether the capture button was released since the last check.
   *
   * @return Whether the button was released since the last check.
   */
  public boolean getCaptureButtonReleased() {
    return getRawButtonReleased(Button.kCapture.value);
  }

  /**
   * Read the value of the bumper button on the controller.
   *
   * @return The state of the button.
   */
  public boolean getBumperButton() {
    return getRawButton(Button.kBumper.value);
  }

  /**
   * Whether the bumper button was pressed since the last check.
   *
   * @return Whether the button was pressed since the last check.
   */
  public boolean getBumperButtonPressed() {
    return getRawButtonPressed(Button.kBumper.value);
  }

  /**
   * Whether the bumper button was released since the last check.
   *
   * @return Whether the button was released since the last check.
   */
  public boolean getBumperButtonReleased() {
    return getRawButtonReleased(Button.kBumper.value);
  }

  /**
   * Read the value of the trigger button on the controller.
   *
   * @return The state of the button.
   */
  public boolean getTriggerButton() {
    return getRawButton(Button.kTrigger.value);
  }

  /**
   * Whether the trigger button was pressed since the last check.
   *
   * @return Whether the button was pressed since the last check.
   */
  public boolean getTriggerButtonPressed() {
    return getRawButtonPressed(Button.kTrigger.value);
  }

  /**
   * Whether the trigger button was released since the last check.
   *
   * @return Whether the button was released since the last check.
   */
  public boolean getTriggerButtonReleased() {
    return getRawButtonReleased(Button.kTrigger.value);
  }

  /**
   * Read the value of the minus button on the controller.
   *
   * @return The state of the button.
   */
  public boolean getMinusButton() {
    return getRawButton(Button.kMinus.value);
  }

  /**
   * Whether the minus button was pressed since the last check.
   *
   * @return Whether the button was pressed since the last check.
   */
  public boolean getMinusButtonPressed() {
    return getRawButtonPressed(Button.kMinus.value);
  }

  /**
   * Whether the minus button was released since the last check.
   *
   * @return Whether the button was released since the last check.
   */
  public boolean getMinusButtonReleased() {
    return getRawButtonReleased(Button.kMinus.value);
  }
}
