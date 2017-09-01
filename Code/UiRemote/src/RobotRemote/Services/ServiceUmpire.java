package RobotRemote.Services;

import RobotRemote.Services.Listeners.Connection.RobotConnectionService;
import RobotRemote.Services.Listeners.Movement.MovementEventListener;
import RobotRemote.Services.Workers.SensorService.SensorsService;
import RobotRemote.Services.Workers.UiUpdater.UiUpdaterService;

public class ServiceUmpire {
  private final UiUpdaterService uiUpdaterService;
  private final RobotConnectionService robotConnectionService;
  private final SensorsService sensorService;
  private final MovementEventListener movementListener;

  public ServiceUmpire(ServiceLocator serviceLocator) {
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
    this.movementListener.closeMotors();
  }
}
