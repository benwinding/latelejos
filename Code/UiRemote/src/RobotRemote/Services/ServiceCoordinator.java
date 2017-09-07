package RobotRemote.Services;

import RobotRemote.Services.Connection.RobotConnectionService;
import RobotRemote.Services.Movement.MovementHandler;
import RobotRemote.Services.Sensors.SensorsService;
import RobotRemote.Services.UiUpdater.UiUpdaterService;

public class ServiceCoordinator {
  private final UiUpdaterService uiUpdaterService;
  private final RobotConnectionService robotConnectionService;
  private final SensorsService sensorService;
  private final MovementHandler movementListener;

  public ServiceCoordinator(RobotConnectionService robotConnectionService, SensorsService sensorService, UiUpdaterService uiUpdaterService, MovementHandler movementListener) {
    this.sensorService = sensorService;
    this.movementListener = movementListener;
    this.uiUpdaterService = uiUpdaterService;
    this.robotConnectionService = robotConnectionService;
  }

  public void StartAllThreads() {
    // Run start up in the background so the GUI can load quicker
    Thread startUp = new Thread(() -> {
      this.uiUpdaterService.start();
      this.robotConnectionService.InitializeBrick();
      this.movementListener.Initialize();
      if(this.robotConnectionService.IsConnected())
        this.sensorService.start();
    });
    startUp.start();
  }

  public void StopAllThreads() {
    this.sensorService.kill();
    this.uiUpdaterService.kill();
    this.movementListener.shutdownMotors();
    this.robotConnectionService.closeConnection();
  }
}
