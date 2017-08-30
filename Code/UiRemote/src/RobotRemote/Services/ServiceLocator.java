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

  public ServiceLocator(RobotRepository robotRepository, RootController guiMainController) {
    robotConnectionService = new RobotConnectionService();
    sensorService = new SensorsService(robotConnectionService, robotRepository.getSensorsState());
    guiUpdaterService = new GuiUpdaterService(robotRepository, guiMainController);
  }

  public SensorsService getSensorService() {
    return sensorService;
  }

  public GuiUpdaterService getGuiUpdaterService() {
    return guiUpdaterService;
  }
}
