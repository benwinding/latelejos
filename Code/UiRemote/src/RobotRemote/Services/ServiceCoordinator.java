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

  public ServiceCoordinator(ServiceLocator serviceLocator) {
    this.sensorService = serviceLocator.getSensorService();
    this.uiUpdaterService = serviceLocator.getUiUpdaterService();
    this.robotConnectionService = serviceLocator.getRobotConnectionService();
    this.movementListener = serviceLocator.getMovementListener();
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
