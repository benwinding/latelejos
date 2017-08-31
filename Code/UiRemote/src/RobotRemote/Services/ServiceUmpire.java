package RobotRemote.Services;

import RobotRemote.Services.Synchronous.Connection.RobotConnectionService;
import RobotRemote.Services.Synchronous.SensorService.SensorsService;
import RobotRemote.Services.Synchronous.UiUpdater.UiUpdaterService;

public class ServiceUmpire {
  private final UiUpdaterService uiUpdaterService;
  private final RobotConnectionService robotConnectionService;
  private final SensorsService sensorService;

  public ServiceUmpire(ServiceLocator serviceLocator) {
    this.sensorService = serviceLocator.getSensorService();
    this.uiUpdaterService = serviceLocator.getUiUpdaterService();
    this.robotConnectionService = serviceLocator.getRobotConnectionService();
  }

  public void StartAllThreads() {
    this.uiUpdaterService.start();
    if(robotConnectionService.IsConnected())
      this.sensorService.start();
  }

  public void StopAllThreads() {
    this.sensorService.kill();
    this.uiUpdaterService.kill();
  }
}
