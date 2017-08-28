package RobotRemote;

import RobotRemote.Models.Interfaces.CustomNavigatorInterface;
import RobotRemote.Models.Interfaces.RobotCoordinateSystemInterface;
import RobotRemote.Utils.Logger;
import lejos.remote.ev3.RemoteRequestEV3;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Pose;

import static lejos.robotics.navigation.MoveController.WHEEL_SIZE_EV3;

public class RobotMotorManager {
  static private CustomNavigatorInterface navigator;
  public static boolean IsDirty;
  public static String OldCommand;

  static ArcRotateMoveController GetPilot() {
    ArcRotateMoveController pilot;
    try {
      RemoteRequestEV3 brick = RobotConnectionManager.GetBrick();
      pilot = brick.createPilot(2.1f, 4.4f, "A", "B");
      pilot.setLinearSpeed(5);
      pilot.setAngularSpeed(30);
      pilot.setAngularAcceleration(10);
      return pilot;
    } catch (Exception e) {
      Logger.Log("Pilot Factory: Unable to get pilot from ev3, using test pilot");
      return null;
    }
  }

  static void InitMotors(float xInit, float yInit, float thetaInit) {
    RobotCoordinateSystemInterface cs = new RobotCoordinateSystem(xInit, yInit, thetaInit);
    ArcRotateMoveController pilot = GetPilot();
    navigator = new CustomNavigator();
    navigator.Init(cs, pilot);
  }

  public static void MoveMotors(String command) {
    Logger.Log("Moving motors: " + command);
    if(OldCommand != "" && OldCommand == command) {
      return;
    }
    OldCommand = command;
      navigator.Stop();
      switch (command) {
      case "Forward":
        navigator.MoveAsync();
        break;
      case "Backward":
        navigator.MoveAsync(true);
        break;
      case "Left":
        navigator.Rotate(90);
        break;
      case "Right":
        navigator.Rotate(-90);
        break;
      case  "Stop":
        break;
      default:
        Logger.Log("Unknown command: " + command);
    }
  }

  public static Pose GetCoords() {
    return navigator.GetGlobalPose();
  }

  public static void StopAll() {
    Logger.Log("Stopping All Motors");
    navigator.Stop();
  }
}
