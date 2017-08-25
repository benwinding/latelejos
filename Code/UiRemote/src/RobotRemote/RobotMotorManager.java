package RobotRemote;

import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Pose;

class RobotMotorManager {
  static private CustomNavigatorInterface navigator;

  static void InitMotors(float xInit, float yInit) {
    RobotCoordinateSystemInterface cs = new RobotCoordinateSystem(xInit, yInit);
    ArcRotateMoveController pilot = PilotFactory.GetPilot();
    navigator = new CustomNavigator();
    navigator.Init(cs, pilot);
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
