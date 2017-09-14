package RobotRemote.Shared;

import RobotRemote.Models.RobotConfiguration;
import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.RobotServices.Movement.IMovementService;
import RobotRemote.RobotServices.Sensors.SensorsService;
import RobotRemote.UIServices.UiUpdater.UiUpdaterService;
import com.google.common.eventbus.EventBus;

public class ServiceManager {
  private UiUpdaterService uiUpdaterService;
  private RobotConnectionService robotConnectionService;
  private RobotConfiguration configuration;
  private AppStateRepository appStateRepository;
  private SensorsService sensorService;
  private IMovementService movementService;
  private EventBus eventBus;
  private ThreadLoop robotStateMachineThread;

  public ServiceManager(EventBus eventBus, RobotConfiguration configuration,
                        AppStateRepository appStateRepository,
                        RobotConnectionService robotConnectionService,
                        SensorsService sensorService,
                        UiUpdaterService uiUpdaterService,
                        IMovementService movementService,
                        ThreadLoop robotStateMachineThread) {
    this.eventBus = eventBus;
    this.configuration = configuration;
    this.appStateRepository = appStateRepository;
    this.movementService = movementService;
    this.sensorService = sensorService;
    this.uiUpdaterService = uiUpdaterService;
    this.robotConnectionService = robotConnectionService;
    this.robotStateMachineThread = robotStateMachineThread;
  }

  public void StartAllThreads() {
    // Run start up in the background so the GUI can load quicker
    Thread startUp = new Thread(() -> {
      Logger.log("Service Coordinator: Starting all threads");
      this.uiUpdaterService.start();
      this.robotConnectionService.InitializeBrick();
      this.movementService.Initialize(configuration, robotConnectionService, appStateRepository);
      if(this.robotConnectionService.IsConnected())
        this.sensorService.start();
    });
    startUp.start();
  }

  public void StopAllThreads() {
    this.robotStateMachineThread.StopThread();
    this.movementService.stop();
    this.sensorService.kill();
    this.uiUpdaterService.kill();
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    this.robotConnectionService.closeConnection();
  }

  public UiUpdaterService getUiUpdaterService() {
    return this.uiUpdaterService;
  }

  public RobotConnectionService getRobotConnectionService() {
    return this.robotConnectionService;
  }

  public RobotConfiguration getConfiguration() {
    return this.configuration;
  }

  public AppStateRepository getAppState() {
    return this.appStateRepository;
  }

  public SensorsService getSensorService() {
    return this.sensorService;
  }

  public IMovementService getMovementService() {
    return this.movementService;
  }

  public EventBus getEventBus() {
    return this.eventBus;
  }

  public ThreadLoop getRobotStateMachineThread() {
    return robotStateMachineThread;
  }
}
