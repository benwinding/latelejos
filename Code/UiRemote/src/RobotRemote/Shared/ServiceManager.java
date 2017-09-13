package RobotRemote.Shared;

import RobotRemote.Models.RobotConfiguration;
import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.RobotServices.Movement.MoveThread;
import RobotRemote.RobotServices.Sensors.SensorsService;
import RobotRemote.UIServices.UiUpdater.UiUpdaterService;

public class ServiceManager {
  private final UiUpdaterService uiUpdaterService;
  private final RobotConnectionService robotConnectionService;
  private RobotConfiguration configuration;
  private AppStateRepository app;
  private final SensorsService sensorService;
  private MoveThread moveThread;

  public ServiceManager(RobotConfiguration configuration, AppStateRepository app, RobotConnectionService robotConnectionService, SensorsService sensorService, UiUpdaterService uiUpdaterService, MoveThread moveThread) {
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
