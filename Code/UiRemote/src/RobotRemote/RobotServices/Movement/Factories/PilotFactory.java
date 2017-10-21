package RobotRemote.RobotServices.Movement.Factories;

import RobotRemote.RobotServices.Movement.Mocks.MockSensor;
import RobotRemote.Shared.AppStateRepository;
import RobotRemote.Shared.Logger;
import RobotRemote.Shared.RobotConfiguration;
import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.RobotServices.Movement.Mocks.TestArcPilot;
import lejos.robotics.navigation.ArcRotateMoveController;

public class PilotFactory {
  public static ArcRotateMoveController GetPilot(RobotConfiguration config, RobotConnectionService connectionService, AppStateRepository app) {
    ArcRotateMoveController pilot;
    if(connectionService.IsConnected()) {
      pilot = connectionService.GetBrick().createPilot(
          config.robotWheelDia,
          config.robotTrackWidth,
          config.wheelPortLeft,
          config.wheelPortRight
      );
      Logger.debug("Robot is connected, using robots pilot");
    }
    else {
      Logger.debug("Robot not connected, using TestArcPilot");
      pilot = new TestArcPilot();
      //Turn on test mode
      config.enableTestData = true;

      MockSensor.InitMockData();
      app.getUserNoGoZoneState().enableTestNGZ();
    }
    pilot.setLinearSpeed(config.robotLinearSpeed_cms);
    pilot.setAngularSpeed(config.robotAngularSpeed_degs);
    pilot.setAngularAcceleration(config.robotAngularAcceleration_degs2);
    return pilot;
  }
}
