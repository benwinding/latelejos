package RobotRemote.Repositories;

import RobotRemote.Models.Enums.EnumCommandManual;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Services.Connection.RobotConnectionState;
import RobotRemote.Services.MapHandlers.UserNoGoZoneState;
import RobotRemote.Services.MapHandlers.UserWaypointsState;
import RobotRemote.Services.Movement.MoveThreads.LocationState;
import RobotRemote.Services.Movement.MoveThreads.MovementState;
import RobotRemote.RobotStateMachine.RobotCommandState;
import RobotRemote.Services.Sensors.DiscoveredColoursState;
import RobotRemote.Services.Sensors.SensorsState;
import RobotRemote.Services.UiUpdater.UiUpdaterState;
import RobotRemote.UI.UiState;
import lejos.robotics.navigation.ArcRotateMoveController;

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
  private RobotConnectionState robotConnectionState;

  public AppStateRepository(RobotConfiguration config) {
    sensorsState = new SensorsState();
    movementState = new MovementState();
    locationState = new LocationState(config.initX, config.initY, config.initTheta);
    discoveredColoursState = new DiscoveredColoursState();
    uiUpdaterState = new UiUpdaterState(config.mapInitZoom, config.mapH, config.mapW);
    robotCommandState = new RobotCommandState();
    uiState = new UiState(EnumCommandManual.Ignore);
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

  public RobotCommandState getStateMachineState() {
    return robotCommandState;
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