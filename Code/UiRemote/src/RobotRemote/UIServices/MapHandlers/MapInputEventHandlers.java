package RobotRemote.UIServices.MapHandlers;

import RobotRemote.Models.MapPoint;
import RobotRemote.RobotStateMachine.Events.AutoSurvey.EventAutomapDetectedObject;
import RobotRemote.RobotStateMachine.Events.Shared.EventSwitchToAutoMap;
import RobotRemote.Shared.Logger;
import RobotRemote.Shared.RobotConfiguration;
import RobotRemote.Shared.ServiceManager;
import RobotRemote.UIServices.Events.*;
import RobotRemote.UIServices.UiUpdater.UiUpdaterState;
import com.google.common.eventbus.Subscribe;
import lejos.robotics.navigation.Pose;

public class MapInputEventHandlers {
  private final UserWaypointsState userWaypointsState;
  private final UserNoGoZoneState userNoGoZoneState;
  private final UiUpdaterState uiUpdaterState;
  private final ServiceManager sm;
  private final RobotConfiguration config;

  public MapInputEventHandlers(ServiceManager sm) {
    sm.getEventBus().register(this);
    this.config = sm.getConfiguration();
    this.sm = sm;
    this.userNoGoZoneState = sm.getAppState().getUserNoGoZoneState();
    this.userWaypointsState = sm.getAppState().getUserWaypointsState();
    this.uiUpdaterState = sm.getAppState().getUiUpdaterState();
  }

  @Subscribe
  public void OnUserAddWaypoint(EventUserAddWaypoint event) {
    MapPoint newPoint = this.GetClickMapLocation(event.getX(), event.getY());
    // Translate mouse coordinates to account for map centering
    Logger.debug(String.format("Received UserAddWaypoint:: x:%.1f, y:%.1f", newPoint.x, newPoint.y));
    userWaypointsState.AddWayPoint(newPoint.x,newPoint.y);
    sm.getEventBus().post(new EventSwitchToAutoMap(newPoint));
  }

  @Subscribe
  public void OnEventAutomapDetectedObject(EventAutomapDetectedObject event) {
    Pose detectedLocation = event.getDetectedPosition();
    this.userNoGoZoneState.AddDetectedObstacle(detectedLocation.getX(), detectedLocation.getY());
  }

  @Subscribe
  public void OnUserMapNgzStart(EventUserMapNgzStart event){
    MapPoint newPoint = this.GetClickMapLocation(event.getX(), event.getY());
    userNoGoZoneState.AddNgzStartPoint(newPoint);
  }

  @Subscribe
  public void OnUserMapNgzMiddle(EventUserMapNgzMiddle event){
    MapPoint newPoint = this.GetClickMapLocation(event.getX(), event.getY());
    userNoGoZoneState.AddNgzMiddlePoint(newPoint);
  }

  @Subscribe
  public void OnUserMapNgzEnd(EventUserMapNgzEnd event){
    userNoGoZoneState.FinishedNgzSet();
  }

  MapPoint GetClickMapLocation(double mouseX, double mouseY) {
    double xTrans = mouseX-uiUpdaterState.getMapDragDeltaX();
    double yTrans = mouseY-uiUpdaterState.getMapDragDeltaY();
    MapPoint newPoint = this.ScaleMouseInputToMap(xTrans, yTrans);
    return newPoint;
  }

  @Subscribe
  public void OnUserMapDragged(EventUserMapDragged event) {
    uiUpdaterState.setMapDraggedDelta(event.getX(), event.getY());
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
    Logger.debug("Received UserZoomChanged: " + uiUpdaterState.getZoomLevel());
  }

  public MapPoint ScaleMouseInputToMap(double mouseX, double mouseY) {
    float mapH = uiUpdaterState.getMapH();
    float mapW = uiUpdaterState.getMapW();
    float zoomLevel = uiUpdaterState.getZoomLevel();
    float pixelsPerCm = config.mapPixelsPerCm;

    // Scale mouse to original map pixel coordinates
    double scaleX = mouseX / zoomLevel;
    double scaleY = mouseY / zoomLevel;

    // Get cm coordinates from scaled coordinates
    double scaleXcm = scaleX / pixelsPerCm;
    double scaleYcm = scaleY / pixelsPerCm;

    // Translate mouse coordinates to account for map centering
    double transXcm = scaleXcm + mapW/2;
    double transYcm = scaleYcm + mapH/2;

    return new MapPoint(transXcm, transYcm);
  }
}
