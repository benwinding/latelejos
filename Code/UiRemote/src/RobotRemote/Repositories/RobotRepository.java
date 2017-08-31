package RobotRemote.Repositories;

import RobotRemote.Models.EnumCommandManual;
import RobotRemote.Models.RobotConfig;
import RobotRemote.Services.Asynchronous.Movement.LocationState;
import RobotRemote.Services.Asynchronous.Movement.MovementState;
import RobotRemote.Services.Asynchronous.StateMachine.DiscoveredColoursState;
import RobotRemote.Services.Asynchronous.StateMachine.StateMachineState;
import RobotRemote.Services.Asynchronous.StateMachine.UserNoGoZoneState;
import RobotRemote.Services.Synchronous.SensorService.SensorsState;
import RobotRemote.Services.Synchronous.UiUpdater.UiUpdaterState;
import RobotRemote.UI.UiState;

public class RobotRepository {
  private UiState uiState;
  private SensorsState sensorsState;
  private MovementState movementState;
  private LocationState locationState;
  private DiscoveredColoursState discoveredColoursState;
  private UserNoGoZoneState userNoGoZoneState;
  private UiUpdaterState uiUpdaterState;
  private StateMachineState stateMachineState;

  public RobotRepository(RobotConfig config) {
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
