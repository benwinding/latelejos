package RobotRemote.Shared;

import RobotRemote.RobotServices.Connection.RobotConnectionState;
import RobotRemote.UIServices.MapHandlers.UserNoGoZoneState;
import RobotRemote.UIServices.MapHandlers.UserWaypointsState;
import RobotRemote.RobotServices.Movement.LocationState;
import RobotRemote.RobotServices.Movement.MovementState;
import RobotRemote.RobotServices.Sensors.DiscoveredColoursState;
import RobotRemote.RobotServices.Sensors.SensorsState;
import RobotRemote.UIServices.UiUpdater.UiUpdaterState;
import RobotRemote.UI.UiState;

public class AppStateRepository {
  private UiState uiState;
  private SensorsState sensorsState;
  private MovementState movementState;
  private LocationState locationState;
  private DiscoveredColoursState discoveredColoursState;
  private UiUpdaterState uiUpdaterState;
  private UserNoGoZoneState userNoGoZoneState;
  private UserWaypointsState userWaypointsState;
  private RobotConnectionState robotConnectionState;

  public AppStateRepository(RobotConfiguration config) {
    sensorsState = new SensorsState();
    movementState = new MovementState();
    locationState = new LocationState(config.initX, config.initY, config.initTheta);
    discoveredColoursState = new DiscoveredColoursState();
    uiUpdaterState = new UiUpdaterState(config.mapInitZoom, config.mapH, config.mapW);
    uiState = new UiState();
    userNoGoZoneState = new UserNoGoZoneState(config.ngzRows, config.ngzCols);
    userWaypointsState = new UserWaypointsState();
    robotConnectionState = new RobotConnectionState();
  }

  public SensorsState getSensorsState() {
    return sensorsState;
  }

  public LocationState getLocationState() {
    return locationState;
  }

  public MovementState getMovementState() {
    return movementState;
  }

  public DiscoveredColoursState getDiscoveredColoursState() {
    return discoveredColoursState;
  }

  public UserNoGoZoneState getUserNoGoZoneState() {
    return userNoGoZoneState;
  }

  public UiUpdaterState getUiUpdaterState() {
    return uiUpdaterState;
  }

  public UiState getUiState() {
    return uiState;
  }

  public UserWaypointsState getUserWaypointsState() {
    return userWaypointsState;
  }

  public RobotConnectionState getRobotConnectionState() {
    return robotConnectionState;
  }
}