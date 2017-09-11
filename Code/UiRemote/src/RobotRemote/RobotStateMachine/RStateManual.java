package RobotRemote.RobotStateMachine;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.Events.EventManualCommand;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.RobotStateMachine.Events.EventSTOP;
import RobotRemote.RobotStateMachine.Events.EventWarnOfObject;
import RobotRemote.Services.Connection.RobotConnectionService;
import RobotRemote.Services.Movement.Factories.PilotFactory;
import RobotRemote.Services.Movement.MoveThreads.MoveStraightThread;
import RobotRemote.Services.Movement.MoveThreads.MoveTurnSynchronous;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lejos.robotics.navigation.ArcRotateMoveController;

public class RStateManual {
  private EventBus eventBus;
  private RobotConfiguration config;
  private RobotConnectionService connectionService;
  private AppStateRepository appState;
  private MoveStraightThread moveStraightThread;
  private MoveTurnSynchronous moveTurnSynchronous;

  public RStateManual(EventBus eventBus, RobotConfiguration config, AppStateRepository appState, RobotConnectionService connectionService) {
    eventBus.register(this);
    this.eventBus = eventBus;
    this.config = config;
    this.appState = appState;
    this.connectionService = connectionService;
  }

  public void Initialize() {
    ArcRotateMoveController pilot = PilotFactory.GetPilot(this.config, this.connectionService);
    this.moveStraightThread = new MoveStraightThread(config, pilot, appState.getLocationState(), appState.getMovementState());
    this.moveTurnSynchronous = new MoveTurnSynchronous(pilot, appState.getLocationState(), appState.getMovementState());
  }

  @Subscribe
  public void OnManualButtonPush(EventManualCommand event) {
    Logger.log("Received Manual Command: " + event.getCommand());
    this.moveStraightThread.kill();
    switch (event.getCommand()) {
      case Forward:
        this.moveStraightThread.MoveForward();
        break;
      case Backward:
        this.moveStraightThread.MoveBackward();
        break;
      case Left:
        this.moveTurnSynchronous.TurnLeft();
        break;
      case Right:
        this.moveTurnSynchronous.TurnRight();
        break;
      case Stop:
      case Ignore:
      default:
        this.eventBus.post(new EventSTOP());
    }
  }

  @Subscribe
  public void OnStop(EventSTOP event) {
    this.moveStraightThread.shutdownMotors();
  }

  @Subscribe
  public void OnDetectObject(EventWarnOfObject event) {
    this.eventBus.post(new EventSTOP());
  }
}
