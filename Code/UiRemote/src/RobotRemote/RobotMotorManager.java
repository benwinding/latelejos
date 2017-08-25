package RobotRemote;

import lejos.remote.ev3.RemoteRequestEV3;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Pose;
import static lejos.robotics.navigation.MoveController.WHEEL_SIZE_EV3;

class RobotMotorManager {
  static private CustomNavigatorInterface navigator;

  static void InitMotors() {
    RemoteRequestEV3 brick = RobotConnectionManager.GetBrick();
    try {
      ArcRotateMoveController pilot = brick.createPilot(WHEEL_SIZE_EV3, 10, "A", "B");
      pilot.setLinearSpeed(5);
      pilot.setAngularSpeed(30);
      pilot.setAngularAcceleration(10);
      RobotCoordinateSystemInterface cs = new RobotCoordinateSystem();
      navigator = new CustomNavigator();
      navigator.Init(cs, pilot);
      Logger.Log("Successfully opened motor ports");
    } catch (Exception e) {
      Logger.Log("Unable to open motor ports");
    }
  }

  static void MoveMotors(String Direction) {
    Logger.Log("Moving motors: " + Direction);
    switch (Direction) {
      case "Forward":
        navigator.MoveAsync(10);
        break;
      case "Backward":
        navigator.MoveAsync(-10);
        break;
      case "Left":
        navigator.Rotate(90);
        break;
      case "Right":
        navigator.Rotate(-90);
        break;
      case "Stop":
        navigator.Stop();
        break;
      default:
        Logger.Log("Unknown Direction: " + Direction);
    }
  }

  static Pose GetCoords() {
    return navigator.GetGlobalPose();
  }
}
