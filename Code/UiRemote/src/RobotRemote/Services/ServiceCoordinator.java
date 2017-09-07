package RobotRemote.Services;

import RobotRemote.Services.Connection.RobotConnectionService;
import RobotRemote.Services.Movement.MovementEventListener;
import RobotRemote.Services.Sensors.SensorsService;
import RobotRemote.Services.UiUpdater.UiUpdaterService;

public class ServiceCoordinator {
  private final UiUpdaterService uiUpdaterService;
  private final RobotConnectionService robotConnectionService;
  private final SensorsService sensorService;
  private final MovementEventListener movementListener;

  public ServiceCoordinator(RobotConnectionService robotConnectionService, SensorsService sensorService, UiUpdaterService uiUpdaterService, MovementEventListener movementListener) {
    this.sensorService = sensorService;
    this.movementListener = movementListener;
    this.uiUpdaterService = uiUpdaterService;
    this.robotConnectionService = robotConnectionService;
  }

  public void StartAllThreads() {
    this.uiUpdaterService.start();
    if(robotConnectionService.IsConnected())
      this.sensorService.start();
  }

  public void StopAllThreads() {
    this.sensorService.kill();
    this.uiUpdaterService.kill();
    this.movementListener.shutdownMotors();
    this.robotConnectionService.closeConnection();
  }
}
