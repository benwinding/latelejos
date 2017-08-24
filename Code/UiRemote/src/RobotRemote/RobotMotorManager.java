package RobotRemote;

import lejos.remote.ev3.RemoteRequestEV3;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Pose;

import static lejos.robotics.navigation.MoveController.WHEEL_SIZE_EV3;

class RobotMotorManager {
  private static ArcRotateMoveController pilot;
  private static RobotCoordinateSystemInterface cs;

  static void InitMotors() {
    RemoteRequestEV3 brick = RobotConnectionManager.GetBrick();
    try {
      pilot = brick.createPilot(WHEEL_SIZE_EV3,10,"A","B");
      pilot.setLinearSpeed(5);
      pilot.setLinearAcceleration(5);
      pilot.setAngularSpeed(30);
      pilot.setAngularAcceleration(10);

      cs = new RobotCoordinateSystem();

      Logger.Log("Successfully opened motor ports");
    } catch (Exception e) {
      Logger.Log("Unable to open motor ports");
    }
  }

  static void MoveMotors(String Direction) {
    if(pilot == null) {
      Logger.Log("ev3 not connected, cannot move:" + Direction);
      return;
    }
    switch (Direction) {
      case "Forward":
        pilot.travel(10);
        cs.GoingForward(10);
        break;
      case "Backward":
        pilot.travel(10, true);
        cs.GoingBackward(10);
        break;
      case "Left":
        pilot.rotate(90);
        cs.ChangingHeading(90);
        break;
      case "Right":
        pilot.rotate(-90);
        cs.ChangingHeading(-90);
        break;
      case "Stop":
        pilot.stop();
        break;
      default:
        Logger.Log("Unknown Direction: " + Direction);
    }
  }

  static Pose GetCoords() {
    return cs.GetGlobalPose();
  }
}
