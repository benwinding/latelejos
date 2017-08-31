package RobotRemote.Services;

import RobotRemote.Services.Listeners.Connection.RobotConnectionService;
import RobotRemote.Services.Workers.SensorService.SensorsService;
import RobotRemote.Services.Workers.UiUpdater.UiUpdaterService;

public class ServiceLocator {
  private RobotConnectionService robotConnectionService;
  private SensorsService sensorService;
  private UiUpdaterService uiUpdaterService;

  public ServiceLocator(RobotConnectionService robotConnectionService, SensorsService sensorService, UiUpdaterService uiUpdaterService) {
    this.robotConnectionService = robotConnectionService;
    this.sensorService = sensorService;
    this.uiUpdaterService = uiUpdaterService;
  }

  public UiUpdaterService getUiUpdaterService() {
    return uiUpdaterService;
  }

  public RobotConnectionService getRobotConnectionService() {
    return robotConnectionService;
  }

  public SensorsService getSensorService() {
    return sensorService;
  }
}
