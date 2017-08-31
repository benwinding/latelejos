package RobotRemote.Repositories;

import RobotRemote.Models.EnumCommandManual;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Services.Listeners.Movement.LocationState;
import RobotRemote.Services.Listeners.Movement.MovementState;
import RobotRemote.Services.Listeners.StateMachine.DiscoveredColoursState;
import RobotRemote.Services.Listeners.StateMachine.StateMachineState;
import RobotRemote.Services.Listeners.StateMachine.UserNoGoZoneState;
import RobotRemote.Services.Workers.SensorService.SensorsState;
import RobotRemote.Services.Workers.UiUpdater.UiUpdaterState;
import RobotRemote.UI.UiState;

public class AppStateRepository {
  private UiState uiState;
  private SensorsState sensorsState;
  private MovementState movementState;
  private LocationState locationState;
  private DiscoveredColoursState discoveredColoursState;
  private UserNoGoZoneState userNoGoZoneState;
  private UiUpdaterState uiUpdaterState;
  private StateMachineState stateMachineState;

  public AppStateRepository(RobotConfiguration config) {
    sensorsState = new SensorsState();
    movementState = new MovementState();
    locationState = new LocationState(config.initX,config.initY,config.initTheta);
    discoveredColoursState = new DiscoveredColoursState();
    userNoGoZoneState = new UserNoGoZoneState(config.ngzRows,config.ngzCols);
    uiUpdaterState = new UiUpdaterState(config.mapInitZoom, config.mapH, config.mapW);
    stateMachineState = new StateMachineState();
    uiState = new UiState(EnumCommandManual.Ignore);
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

  public StateMachineState getStateMachineState() {
    return stateMachineState;
  }

  public UiState getUiState() {
    return uiState;
  }
}
