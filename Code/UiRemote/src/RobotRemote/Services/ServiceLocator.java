package RobotRemote.Services;

import RobotRemote.Repositories.RobotRepository;
import RobotRemote.Services.Asynchronous.Movement.MovementEventListener;
import RobotRemote.Services.Synchronous.Connection.RobotConnectionService;
import RobotRemote.Services.Synchronous.SensorService.SensorsService;
import RobotRemote.Services.Synchronous.UiUpdater.UiUpdaterService;
import RobotRemote.UI.Views.RootController;

public class ServiceLocator {
  private final SensorsService sensorService;
  private final RobotConnectionService robotConnectionService;
  private MovementEventListener movementService;
  private final UiUpdaterService uiUpdaterService;

  public ServiceLocator(RobotConnectionService robotConnectionService, RobotRepository robotRepository, RootController guiMainController, MovementEventListener movementService) {
    this.robotConnectionService = robotConnectionService;
    this.movementService = movementService;
    this.sensorService = new SensorsService(robotConnectionService, robotRepository.getSensorsState());
    this.uiUpdaterService = new UiUpdaterService(robotRepository, guiMainController);
  }

  public SensorsService getSensorService() {
    return sensorService;
  }

  public UiUpdaterService getUiUpdaterService() {
    return uiUpdaterService;
  }

  public RobotConnectionService getRobotConnectionService() {
    return robotConnectionService;
  }

  public MovementEventListener getMovementService() {
    return movementService;
  }
}
