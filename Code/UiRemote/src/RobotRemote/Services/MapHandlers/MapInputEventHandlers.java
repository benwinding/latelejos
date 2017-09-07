package RobotRemote.Services.MapHandlers;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.Events.EventUserAddNgz;
import RobotRemote.Models.Events.EventUserAddWaypoint;
import RobotRemote.Models.Events.EventUserZoomChanged;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.Services.UiUpdater.UiUpdaterState;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class MapInputEventHandlers {
  private final UserWaypointsState userWaypointsState;
  private final UserNoGoZoneState userNoGoZoneState;
  private final UiUpdaterState uiUpdaterState;

  public MapInputEventHandlers(AppStateRepository appStateRepository, EventBus eventBus) {
    eventBus.register(this);
    this.userNoGoZoneState = appStateRepository.getUserNoGoZoneState();
    this.userWaypointsState = appStateRepository.getUserWaypointsState();
    this.uiUpdaterState = appStateRepository.getUiUpdaterState();
  }

  @Subscribe
  public void OnUserAddWaypoint(EventUserAddWaypoint event) {
    // Account for zoom on map
    float mapH = uiUpdaterState.getMapH();
    float mapW = uiUpdaterState.getMapW();
    float zoomLevel = uiUpdaterState.getZoomLevel();

    // Mouse relative coordinates to scaled map
    double mouseX = event.getX()-((1-zoomLevel)/2)*mapW;
    double mouseY = event.getY()-((1-zoomLevel)/2)*mapH;

    // Scale mouse to actual map xy coordinates
    double scaleX = mouseX / zoomLevel;
    double scaleY = mouseY / zoomLevel;
    Logger.log("Received UserAddNgz, x:" + scaleX + ", y:" + scaleY);

    userWaypointsState.AddWayPoint(scaleX,scaleY);
  }

  @Subscribe
  public void OnUserAddNgz(EventUserAddNgz event) {
    // Account for zoom on map
    float mapH = uiUpdaterState.getMapH();
    float mapW = uiUpdaterState.getMapW();
    float zoomLevel = uiUpdaterState.getZoomLevel();

    // Mouse relative coordinates to scaled map
    double mouseX = event.getX()-((1-zoomLevel)/2)*mapW;
    double mouseY = event.getY()-((1-zoomLevel)/2)*mapH;

    // Scale mouse to actual map xy coordinates
    double scaleX = mouseX / zoomLevel;
    double scaleY = mouseY / zoomLevel;

    Logger.log("Received UserAddNgz, x:" + scaleX + ", y:" + scaleY);
    int cols = userNoGoZoneState.countGridRows();
    int rows = userNoGoZoneState.countGridCols();

    int r = this.GetCellInRange(mapW, cols, scaleX);
    int c = this.GetCellInRange(mapH, rows, scaleY);
    userNoGoZoneState.switchNgzCell(r,c);
  }

  @Subscribe
  public void OnUserChangeZoom(EventUserZoomChanged event) {
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
    Logger.log("Event: Zoom factor changed to: " + uiUpdaterState.getZoomLevel());
  }

  // Get the cell selected in a certain range
  private int GetCellInRange(double distLength, int cellCount, double distPoint) {
    double cellWidth = distLength/cellCount;
    double cellsOver = distPoint / cellWidth;
    double cell = Math.floor(cellsOver);
    return (int) cell;
  }
}
