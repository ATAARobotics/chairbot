package ca.fourthreethreefour.joysticks;

import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.hal.HAL;

/**
 * Handle input from Nintendo Switch Joycon Right controller connected to the Driver Station.
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
public class JoyconRightController extends GenericHID {
  /**
   * Represents a digital button on an XboxController.
   */
  private enum Button {
    kA(1),
    kX(2),
    kB(3),
    kY(4),
    kBumperLeft(5),
    kBumperRight(6),
    kPlus(10),
    kStick(12),
    kHome(13),
    kBumper(15),
    kTrigger(16);

    @SuppressWarnings({"MemberName", "PMD.SingularField"})
    private final int value;

    Button(int value) {
      this.value = value;
    }
  }

  /**
   * Which button being specified.
   */
  public enum Letter {
    kA(0), kB(1), kX(2), kY(3);

    @SuppressWarnings("MemberName")
    public final int value;

    Letter(int value) {
      this.value = value;
    }
  }

  /**
   * Construct an instance of a joystick. The joystick index is the USB port on the drivers
   * station.
   *
   * @param port The port on the Driver Station that the joystick is plugged into.
   */
  public JoyconRightController(final int port) {
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
   * @param letter Which button to press. A B X or Y.
   * @return The state of the button. If no button is properly specified, returns false.
   */
  public boolean getButton(Letter letter) {
    switch(letter) {
      case kB: 
        return getRawButton(Button.kB.value);
      case kX:
        return getRawButton(Button.kX.value);
      case kA:
        return getRawButton(Button.kA.value);
      case kY:
        return getRawButton(Button.kY.value);
      default:
        return false;
    }
  }

  /**
   * Whether the specified button was pressed since the last check.
   *
   * @param letter Which button to press. A B X or Y.
   * @return Whether the button was pressed since the last check. If no button is properly specified, returns false.
   */
  public boolean getButtonPressed(Letter letter) {
    switch(letter) {
      case kB: 
        return getRawButtonPressed(Button.kB.value);
      case kX:
        return getRawButtonPressed(Button.kX.value);
      case kA:
        return getRawButtonPressed(Button.kA.value);
      case kY:
        return getRawButtonPressed(Button.kY.value);
      default:
        return false;
    }
  }

  /**
   * Whether the specified button was released since the last check.
   *
   * @param letter Which button to press. A B X or Y.
   * @return Whether the button was released since the last check. If no button is properly specified, returns false.
   */
  public boolean getButtonReleased(Letter letter) {
    switch(letter) {
      case kB: 
        return getRawButtonReleased(Button.kB.value);
      case kX:
        return getRawButtonReleased(Button.kX.value);
      case kA:
        return getRawButtonReleased(Button.kA.value);
      case kY:
        return getRawButtonReleased(Button.kY.value);
      default:
        return false;
    }
  }

  /**
   * Read the value of the home button on the controller.
   *
   * @return The state of the button.
   */
  public boolean getHomeButton() {
    return getRawButton(Button.kHome.value);
  }

  /**
   * Whether the home button was pressed since the last check.
   *
   * @return Whether the button was pressed since the last check.
   */
  public boolean getHomeButtonPressed() {
    return getRawButtonPressed(Button.kHome.value);
  }

  /**
   * Whether the home button was released since the last check.
   *
   * @return Whether the button was released since the last check.
   */
  public boolean getHomeButtonReleased() {
    return getRawButtonReleased(Button.kHome.value);
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
   * Read the value of the plus button on the controller.
   *
   * @return The state of the button.
   */
  public boolean getPlusButton() {
    return getRawButton(Button.kPlus.value);
  }

  /**
   * Whether the plus button was pressed since the last check.
   *
   * @return Whether the button was pressed since the last check.
   */
  public boolean getPlusButtonPressed() {
    return getRawButtonPressed(Button.kPlus.value);
  }

  /**
   * Whether the plus button was released since the last check.
   *
   * @return Whether the button was released since the last check.
   */
  public boolean getPlusButtonReleased() {
    return getRawButtonReleased(Button.kPlus.value);
  }
}
