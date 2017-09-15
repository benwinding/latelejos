package RobotRemote.RobotServices.Movement.Factories;

import RobotRemote.Shared.Logger;
import RobotRemote.Shared.RobotConfiguration;
import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.RobotServices.Movement.Mocks.TestArcPilot;
import lejos.robotics.navigation.ArcRotateMoveController;

public class PilotFactory {
  public static ArcRotateMoveController GetPilot(RobotConfiguration config, RobotConnectionService connectionService) {
    ArcRotateMoveController pilot;
    if(connectionService.IsConnected()) {
      pilot = connectionService.GetBrick().createPilot(
          config.robotWheelDia,
          config.robotTrackWidth,
          config.wheelPortLeft,
          config.wheelPortRight
      );
      Logger.log("Robot is connected, using robots pilot");
    }
    else {
      Logger.log("Robot not connected, using TestArcPilot");
      pilot = new TestArcPilot();
    }
    pilot.setLinearSpeed(config.robotLinearSpeed_cms);
    pilot.setAngularSpeed(config.robotAngularSpeed_degs);
    pilot.setAngularAcceleration(config.robotAngularAcceleration_degs2);
    return pilot;
  }
}
