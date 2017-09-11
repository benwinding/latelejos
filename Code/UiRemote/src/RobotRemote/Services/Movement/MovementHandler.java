package RobotRemote.Services.Movement;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.Events.EventAutoControl;
import RobotRemote.Models.Events.EventAutonomousControl;
import RobotRemote.Models.Events.EventManualControl;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.Services.Connection.RobotConnectionService;
import RobotRemote.Services.Movement.Factories.PilotFactory;
import RobotRemote.Services.Movement.MoveThreads.MovePreciseThread;
import RobotRemote.Services.Movement.MoveThreads.MoveStraightThread;
import RobotRemote.Services.Movement.MoveThreads.MoveTurnSynchronous;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Waypoint;

public final class MovementHandler {
  private RobotConfiguration config;
  private RobotConnectionService connectionService;
  private AppStateRepository appState;
  private MoveStraightThread moveStraightThread;
  private MoveTurnSynchronous moveTurnSynchronous;
  private MovePreciseThread movePreciseThread;
  public MovementHandler(EventBus eventBus, RobotConfiguration config, AppStateRepository appState, RobotConnectionService connectionService) {
    eventBus.register(this);
    this.config = config;
    this.connectionService = connectionService;
    this.appState = appState;
  }

  public void Initialize() {
    ArcRotateMoveController pilot = PilotFactory.GetPilot(this.config, this.connectionService);
    this.moveStraightThread = new MoveStraightThread(config, pilot, appState.getLocationState(), appState.getMovementState());
    this.moveTurnSynchronous = new MoveTurnSynchronous(pilot, appState.getLocationState(), appState.getMovementState());
    this.movePreciseThread = new MovePreciseThread(pilot, appState.getLocationState(), appState.getMovementState());
    appState.setPilot(pilot);
  }

  @Subscribe
  public void OnManualControl(EventManualControl event) {
    Logger.log("Received Manual Command: " + event.getCommand());
    this.movePreciseThread.kill();
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
    }
  }


    @Subscribe
    public void OnAutonomousControl(EventAutonomousControl event) {
        Logger.log("Received Manual Command: " + event.getCommand());
        this.movePreciseThread.kill();
        this.moveStraightThread.kill();
        switch (event.getCommand()) {
            case Forward:
                this.moveStraightThread.MoveDistance(event.getDistance());
                break;
            case Backward:
                this.moveStraightThread.MoveDistance(-event.getDistance());
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
        }
    }
  @Subscribe
  public void OnPreciseControl(EventAutoControl event) {
    Waypoint eventWayPoint = event.getNextWayPoint();
    // Account for zoom on map
    float mapH = this.appState.getUiUpdaterState().getMapH();
    float mapW = this.appState.getUiUpdaterState().getMapW();
    float zoomLevel = this.appState.getUiUpdaterState().getZoomLevel();

    // Mouse relative coordinates to scaled map
    double mouseX = eventWayPoint.getX()/config.mapPixelsPerCm - ((1-zoomLevel)/2)*mapW;
    double mouseY = eventWayPoint.getY()/config.mapPixelsPerCm - ((1-zoomLevel)/2)*mapH;

    // Scale mouse to actual map xy coordinates
    double scaleX = mouseX / zoomLevel;
    double scaleY = mouseY / zoomLevel;
    Waypoint nextWayPoint = new Waypoint(scaleX, scaleY);

    Logger.log(String.format("Received Precise Point to go to:: x:%.1f, y:%.1f", nextWayPoint.getX(), nextWayPoint.getY()));
    this.movePreciseThread.kill();
    this.moveStraightThread.kill();
    this.movePreciseThread.moveToWaypoint(nextWayPoint);
  }

  public void shutdownMotors() {
    this.moveStraightThread.shutdownMotors();
  }
}
