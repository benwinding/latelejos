package RobotRemote.Services;

import RobotRemote.Repositories.RobotRepository;
import RobotRemote.Services.Synchronous.Connection.RobotConnectionService;
import RobotRemote.Services.Synchronous.GuiUpdater.GuiUpdaterService;
import RobotRemote.Services.Synchronous.SensorService.SensorsService;
import RobotRemote.UI.Views.RootController;

public class ServiceLocator {
  private final SensorsService sensorService;
  private final RobotConnectionService robotConnectionService;
  private final GuiUpdaterService guiUpdaterService;

  public ServiceLocator(RobotConnectionService robotConnectionService, RobotRepository robotRepository, RootController guiMainController) {
    this.robotConnectionService = robotConnectionService;
    this.sensorService = new SensorsService(robotConnectionService, robotRepository.getSensorsState());
    this.guiUpdaterService = new GuiUpdaterService(robotRepository, guiMainController);
  }

  public SensorsService getSensorService() {
    return sensorService;
  }

  public GuiUpdaterService getGuiUpdaterService() {
    return guiUpdaterService;
  }
}
