package RobotRemote.Services.Mocks;

import RobotRemote.Models.MoveCommand;
import RobotRemote.Services.CustomCoordinateSystem;
import RobotRemote.Utils.Logger;
import lejos.robotics.navigation.Pose;

public class TestingMoveService {
  private static CustomCoordinateSystem cs;

  public static void InitMotors(float xInit, float yInit, float thetaInit) {
    cs = new CustomCoordinateSystem(xInit, yInit, thetaInit);
  }

  public static void MoveMotors(MoveCommand command) {
    switch (command) {
      case Forward:
        cs.GoingStraight(5);
        break;
      case Backward:
        cs.GoingStraight(-5);
        break;
      case Left:
        cs.ChangingHeading(-90);
        break;
      case Right:
        cs.ChangingHeading(90);
        break;
      case  Stop:
        break;
      default:
        Logger.Log("Unknown command: " + command);
    }
  }

  public static Pose GetCoords() {
    return cs.GetGlobalPose();
  }
}
