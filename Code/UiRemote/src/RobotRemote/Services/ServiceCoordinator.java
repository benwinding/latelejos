package RobotRemote.Services;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.Services.Connection.RobotConnectionService;
import RobotRemote.Services.Movement.MoveThreads.MoveThread;
import RobotRemote.Services.Sensors.SensorsService;
import RobotRemote.Services.UiUpdater.UiUpdaterService;

public class ServiceCoordinator {
  private final UiUpdaterService uiUpdaterService;
  private final RobotConnectionService robotConnectionService;
  private RobotConfiguration configuration;
  private AppStateRepository app;
  private final SensorsService sensorService;
  private MoveThread moveThread;

  public ServiceCoordinator(RobotConfiguration configuration, AppStateRepository app, RobotConnectionService robotConnectionService, SensorsService sensorService, UiUpdaterService uiUpdaterService, MoveThread moveThread) {
    this.configuration = configuration;
    this.app = app;
    this.moveThread = moveThread;
    this.sensorService = sensorService;
    this.uiUpdaterService = uiUpdaterService;
    this.robotConnectionService = robotConnectionService;
  }

  public void StartAllThreads() {
    // Run start up in the background so the GUI can load quicker
    Thread startUp = new Thread(() -> {
      Logger.log("Service Coordinator: Starting all threads");
      this.uiUpdaterService.start();
      this.robotConnectionService.InitializeBrick();
      this.moveThread.Initialize(configuration, robotConnectionService, app);
      if(this.robotConnectionService.IsConnected())
        this.sensorService.start();
    });
    startUp.start();
  }

  public void StopAllThreads() {
    this.sensorService.kill();
    this.uiUpdaterService.kill();
    this.moveThread.Stop();
    this.robotConnectionService.closeConnection();
  }
}
