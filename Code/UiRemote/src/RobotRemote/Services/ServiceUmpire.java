package RobotRemote.Services;

import RobotRemote.Services.Synchronous.GuiUpdater.GuiUpdaterService;
import RobotRemote.Services.Synchronous.SensorService.SensorsService;

public class ServiceUmpire {
  private final GuiUpdaterService guiUpdaterService;
  private SensorsService sensorService;

  public ServiceUmpire(ServiceLocator serviceLocator) {
    this.sensorService = serviceLocator.getSensorService();
    this.guiUpdaterService = serviceLocator.getGuiUpdaterService();
  }

  public void StartAllThreads() {
    this.sensorService.start();
    this.guiUpdaterService.start();
  }

  public void StopAllThreads() {
    this.sensorService.kill();
    this.guiUpdaterService.kill();
  }
}
