package RobotRemote.Services;

import RobotRemote.Repositories.RobotRepository;
import RobotRemote.Services.Synchronous.Connection.RobotConnectionService;
import RobotRemote.Services.Synchronous.SensorService.SensorsService;

public class ServiceLocator {
  private final SensorsService sensorService;
  private final RobotConnectionService robotConnectionService;

  public ServiceLocator(RobotRepository robotRepository) {
    robotConnectionService = new RobotConnectionService();
    sensorService = new SensorsService(robotConnectionService, robotRepository.getSensorsState());
  }

  public SensorsService getSensorService() {
    return sensorService;
  }
}
