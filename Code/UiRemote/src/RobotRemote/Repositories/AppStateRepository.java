package RobotRemote.Repositories;

import RobotRemote.Models.Enums.EnumCommandManual;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Services.MapHandlers.UserNoGoZoneState;
import RobotRemote.Services.MapHandlers.UserWaypointsState;
import RobotRemote.Services.Movement.LocationState;
import RobotRemote.Services.Movement.MovementState;
import RobotRemote.Services.RobotCommander.RobotCommandState;
import RobotRemote.Services.Sensors.DiscoveredColoursState;
import RobotRemote.Services.Sensors.SensorsState;
import RobotRemote.Services.UiUpdater.UiUpdaterState;
import RobotRemote.UI.UiState;

public class AppStateRepository {
  private UiState uiState;
  private SensorsState sensorsState;
  private MovementState movementState;
  private LocationState locationState;
  private DiscoveredColoursState discoveredColoursState;
  private UiUpdaterState uiUpdaterState;
  private RobotCommandState robotCommandState;
  private UserNoGoZoneState userNoGoZoneState;
  private UserWaypointsState userWaypointsState;

  public AppStateRepository(RobotConfiguration config) {
    sensorsState = new SensorsState();
    movementState = new MovementState();
    locationState = new LocationState(config.initX,config.initY,config.initTheta);
    discoveredColoursState = new DiscoveredColoursState();
    uiUpdaterState = new UiUpdaterState(config.mapInitZoom, config.mapH, config.mapW);
    robotCommandState = new RobotCommandState();
    uiState = new UiState(EnumCommandManual.Ignore);
    userNoGoZoneState = new UserNoGoZoneState(config.ngzRows,config.ngzCols);
    userWaypointsState = new UserWaypointsState();
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

  public RobotCommandState getStateMachineState() {
    return robotCommandState;
  }

  public UiState getUiState() {
    return uiState;
  }

  public UserWaypointsState getUserWaypointsState() {
    return userWaypointsState;
  }
}
