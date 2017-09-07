package RobotRemote.Services;

import RobotRemote.Services.Listeners.Connection.RobotConnectionService;
import RobotRemote.Services.Listeners.Movement.MovementEventListener;
import RobotRemote.Services.SensorService.SensorsService;
import RobotRemote.Services.UiUpdater.UiUpdaterService;

public class ServiceLocator {
  private RobotConnectionService robotConnectionService;
  private SensorsService sensorService;
  private UiUpdaterService uiUpdaterService;
  private MovementEventListener movementListener;

  public ServiceLocator(RobotConnectionService robotConnectionService, SensorsService sensorService, UiUpdaterService uiUpdaterService, MovementEventListener movementListener) {
    this.robotConnectionService = robotConnectionService;
    this.sensorService = sensorService;
    this.uiUpdaterService = uiUpdaterService;
    this.movementListener = movementListener;
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

  public MovementEventListener getMovementListener() {
    return movementListener;
  }
}
