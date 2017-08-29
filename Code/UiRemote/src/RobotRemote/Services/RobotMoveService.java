package RobotRemote.Services;

import RobotRemote.Utils.Logger;
import lejos.remote.ev3.RemoteRequestEV3;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Pose;

public class RobotMoveService {
  static private ICustomNavigator navigator;
  public static boolean IsDirty;
  public static String PreCommand;

  static ArcRotateMoveController GetPilot() {
    ArcRotateMoveController pilot;
    try {
      RemoteRequestEV3 brick = RobotConnectionService.GetBrick();
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

  public static void InitMotors(float xInit, float yInit, float thetaInit) {
    ICustomCoordinateSystem cs = new CustomCoordinateSystem(xInit, yInit, thetaInit);
    ArcRotateMoveController pilot = GetPilot();
    navigator = new CustomNavigator();
    navigator.Init(cs, pilot);
  }

  public static void MoveMotors(String command) {
    Logger.Log("Moving motors: " + command);
    if(PreCommand== command && (PreCommand=="Forward"||PreCommand =="Backward"))
        return;
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
