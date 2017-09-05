package RobotRemote.Services.Listeners.StateMachine;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.Events.*;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.Services.Workers.UiUpdater.UiUpdaterState;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class StateMachineListener{
  private AppStateRepository appStateRepository;
  private ModeObjectAvoidance modeObjectAvoidance;
  private ModeAutoMapping modeAutomapping;
  private StateMachineState stateMachineState;
  private UserNoGoZoneState userNoGoZoneState;
  private UserWaypointsState userWaypointsState;

  public StateMachineListener(AppStateRepository appStateRepository, EventBus eventBus) {
    eventBus.register(this);
    this.appStateRepository = appStateRepository;
    this.stateMachineState = appStateRepository.getStateMachineState();
    this.userNoGoZoneState = appStateRepository.getUserNoGoZoneState();
    this.userWaypointsState = appStateRepository.getUserWaypointsState();
    this.modeAutomapping = new ModeAutoMapping();
    this.modeObjectAvoidance = new ModeObjectAvoidance();
  }

  @Subscribe
  public void OnChangeMode(EventChangeOperationMode event) {
    Logger.LogCrossThread("Received EventChangeOperationMode: " + event.getOperationMode());
    stateMachineState.setCurrentState(event.getOperationMode());
    switch (event.getOperationMode()) {
      case ManualMode:
        this.modeObjectAvoidance.kill();
        this.modeAutomapping.kill();
        break;
      case AutoMode:
        this.modeAutomapping.start();
        break;
      case AvoidanceMode:
        this.modeAutomapping.kill();
        this.modeObjectAvoidance.start();
        break;
      case Waiting:
        this.modeAutomapping.kill();
        this.modeObjectAvoidance.start();
      default:
        break;
    }
  }

  @Subscribe
  public void OnUserAddWaypoint(EventUserAddWaypoint event) {
    Logger.LogCrossThread("Received UserAddNgz, x:" + event.getX() + ", y:" + event.getY());
    // Account for zoom on map
    float mapH = this.appStateRepository.getUiUpdaterState().getMapH();
    float mapW = this.appStateRepository.getUiUpdaterState().getMapW();
    float zoomLevel = this.appStateRepository.getUiUpdaterState().getZoomLevel();

    // Mouse relative coordinates to scaled map
    double mouseX = event.getX()-((1-zoomLevel)/2)*mapW;
    double mouseY = event.getY()-((1-zoomLevel)/2)*mapH;

    // Scale mouse to actual map xy coordinates
    double scaleX = mouseX / zoomLevel;
    double scaleY = mouseY / zoomLevel;

    userWaypointsState.AddWayPoint(scaleX,scaleY);
  }

  @Subscribe
  public void OnUserAddNgz(EventUserAddNgz event) {
    Logger.LogCrossThread("Received UserAddNgz, x:" + event.getX() + ", y:" + event.getY());
    // Account for zoom on map
    float mapH = this.appStateRepository.getUiUpdaterState().getMapH();
    float mapW = this.appStateRepository.getUiUpdaterState().getMapW();
    float zoomLevel = this.appStateRepository.getUiUpdaterState().getZoomLevel();

    // Mouse relative coordinates to scaled map
    double mouseX = event.getX()-((1-zoomLevel)/2)*mapW;
    double mouseY = event.getY()-((1-zoomLevel)/2)*mapH;

    // Scale mouse to actual map xy coordinates
    double scaleX = mouseX / zoomLevel;
    double scaleY = mouseY / zoomLevel;

    int cols = this.appStateRepository.getUserNoGoZoneState().countGridRows();
    int rows = this.appStateRepository.getUserNoGoZoneState().countGridCols();

    int r = this.GetCellInRange(mapW, cols, scaleX);
    int c = this.GetCellInRange(mapH, rows, scaleY);
    userNoGoZoneState.switchNgzCell(r,c);
  }

  @Subscribe
  public void OnUserChangeZoom(EventUserZoomChanged event) {
    UiUpdaterState uiUpdaterState = this.appStateRepository.getUiUpdaterState();
    switch (event.getZoomCommand()){
      case IncrementZoom:
        uiUpdaterState.incrementZoomLevel();
        break;
      case DecrementZoom:
        uiUpdaterState.decrementZoomLevel();
        break;
      case ZoomReset:
        uiUpdaterState.zoomReset();
        break;
    }
    Logger.LogCrossThread("Event: Zoom factor changed to: " + uiUpdaterState.getZoomLevel());
  }

  @Subscribe
  public void OnEventRobotmode(EventRobotmode event) {
    Logger.LogCrossThread("Robot mode changed");

  }

  // Get the cell selected in a certain range
  private int GetCellInRange(double distLength, int cellCount, double distPoint) {
    double cellWidth = distLength/cellCount;
    double cellsOver = distPoint / cellWidth;
    double cell = Math.floor(cellsOver);
    return (int) cell;
  }
}
