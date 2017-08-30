package RobotRemote.Services;

import RobotRemote.Services.Synchronous.SensorService.SensorsService;

public class ServiceUmpire {
  private SensorsService sensorService;

  public ServiceUmpire(ServiceLocator serviceLocator) {
    this.sensorService = serviceLocator.getSensorService();
  }

  public void StartAllThreads() {
    this.sensorService.start();
  }

  public void StopAllThreads() {
    this.sensorService.kill();
  }
}
