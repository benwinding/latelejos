package RobotRemote.Services.Movement;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Services.Connection.RobotConnectionService;
import RobotRemote.Services.Movement.Mocks.TestArcPilot;
import lejos.robotics.navigation.ArcRotateMoveController;

public class PilotFactory {
  public static ArcRotateMoveController GetPilot(RobotConnectionService connectionService, RobotConfiguration config) {
    connectionService.InitializeBrick();
    ArcRotateMoveController pilot;
    if(connectionService.IsConnected()) {
      pilot = connectionService.GetBrick().createPilot(
          config.robotWheelDia,
          config.robotTrackWidth,
          config.wheelPortLeft,
          config.wheelPortRight
      );
      Logger.Log("Robot is connected, using robots pilot");
    }
    else {
      Logger.Log("Robot not connected, using TestArcPilot");
      pilot = new TestArcPilot();
    }
    pilot.setLinearSpeed(config.robotLinearSpeed_cms);
    pilot.setAngularSpeed(config.robotAngularSpeed_degs);
    pilot.setAngularAcceleration(config.robotAngularAcceleration_degs2);
    return pilot;
  }
}
