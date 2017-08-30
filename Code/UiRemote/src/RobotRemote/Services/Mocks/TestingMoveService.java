package RobotRemote.Services.Mocks;

import RobotRemote.Models.MoveCommand;
import RobotRemote.Models.RobotConfig;
import RobotRemote.Services.Asynchronous.Movement.CustomCoordinateSystem;
import RobotRemote.Helpers.Logger;
import RobotRemote.Services.Synchronous.Connection.RobotConnectionService;
import lejos.robotics.navigation.Pose;

public class TestingMoveService {
  private static CustomCoordinateSystem cs;

  public TestingMoveService(RobotConfig config, RobotConnectionService robotConnectionService) {
    cs = new CustomCoordinateSystem(config.initX, config.initY, config.initTheta);
  }

  public void MoveMotors(MoveCommand command) {
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

  public Pose GetCoords() {
    return cs.GetGlobalPose();
  }
}
