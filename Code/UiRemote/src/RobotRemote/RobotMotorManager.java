package RobotRemote;

import lejos.remote.ev3.RemoteRequestEV3;
import lejos.robotics.navigation.ArcRotateMoveController;

public class RobotMotorManager {
  private static ArcRotateMoveController pilot;

  public static void ShutdownMotors() {
    try {
      pilot.stop();
      System.out.println("Successfully closed motor ports");
    } catch (Exception e) {
      System.out.println("Unable to close motor ports");
    }
  }

  public static void InitMotors() {
    RemoteRequestEV3 brick = RobotConnectionManager.GetBrick();
    try {
      pilot = brick.createPilot(41.6,41.6,"A","B");
      pilot.setLinearSpeed(40);

      System.out.println("Successfully opened motor ports");
    } catch (Exception e) {
      System.out.println("Unable to open motor ports");
    }
  }

  static void MoveMotors(String Direction) {
    switch (Direction) {
      case "Forward":
        pilot.forward();
        break;
      case "Backward":
        pilot.backward();
        break;
      case "Left":
        pilot.rotate(90);
        break;
      case "Right":
        pilot.rotate(-90);
        break;
      case "Stop":
        pilot.stop();
        break;
      default:
        System.out.println("Unknown Direction: " + Direction);
    }
  }
}
