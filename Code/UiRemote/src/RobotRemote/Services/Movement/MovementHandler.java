package RobotRemote.Services.Movement;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.Events.EventAutoControl;
import RobotRemote.Models.Events.EventManualControl;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.Services.Connection.RobotConnectionService;
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

  public MovementHandler(RobotConfiguration config, RobotConnectionService connectionService, AppStateRepository appState, EventBus eventBus) {
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
  }

  @Subscribe
  public void OnManualControl(EventManualControl event) {
    Logger.log("Received Manual Command: " + event.getCommand());
    this.movePreciseThread.kill();
    switch (event.getCommand()) {
      case Forward:
        this.moveStraightThread.MoveForward();
        break;
      case Backward:
        this.moveStraightThread.MoveBackward();
        break;
      case Left:
        this.moveStraightThread.kill();
        this.moveTurnSynchronous.TurnLeft();
        break;
      case Right:
        this.moveStraightThread.kill();
        this.moveTurnSynchronous.TurnRight();
        break;
      case Stop:
      case Ignore:
      default:
        this.moveStraightThread.kill();
        this.movePreciseThread.kill();
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
    double mouseX = eventWayPoint.getX()-((1-zoomLevel)/2)*mapW;
    double mouseY = eventWayPoint.getY()-((1-zoomLevel)/2)*mapH;

    // Scale mouse to actual map xy coordinates
    double scaleX = mouseX / zoomLevel;
    double scaleY = mouseY / zoomLevel;
    Waypoint nextWayPoint = new Waypoint(scaleX, scaleY);

    Logger.log("Received Precise Point to go to:: x:"+nextWayPoint.getX() + ",y:" + nextWayPoint.getY());
    this.moveStraightThread.kill();
    this.movePreciseThread.kill();
    this.movePreciseThread.moveToWaypoint(nextWayPoint);
  }

  public void shutdownMotors() {
    this.moveStraightThread.shutdownMotors();
  }
}
