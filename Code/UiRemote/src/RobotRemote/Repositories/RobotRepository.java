package RobotRemote.Repositories;

import RobotRemote.Models.RobotConfig;
import RobotRemote.Repositories.State.*;

public class RobotRepository {
  private SensorsState sensorsState;
  private MotorsState motorsState;
  private LocationState locationState;
  private DiscoveredColoursState discoveredColoursState;
  private UserNoGoZoneState userNoGoZoneState;
  private MapState mapState;
  private StateMachineState stateMachineState;

  public RobotRepository(RobotConfig config) {
    sensorsState = new SensorsState();
    motorsState = new MotorsState();
    locationState = new LocationState(config.initX,config.initY,config.initTheta);
    discoveredColoursState = new DiscoveredColoursState();
    userNoGoZoneState = new UserNoGoZoneState(config.ngzRows,config.ngzCols);
    mapState = new MapState(config.mapInitZoom, config.mapH, config.mapW);
    stateMachineState = new StateMachineState();
  }

  public SensorsState getSensorsState() {
    return sensorsState;
  }

  public LocationState getLocationState() {
    return locationState;
  }

  public MotorsState getMotorsState() {
    return motorsState;
  }

  public DiscoveredColoursState getDiscoveredColoursState() {
    return discoveredColoursState;
  }

  public UserNoGoZoneState getUserNoGoZoneState() {
    return userNoGoZoneState;
  }

  public MapState getMapState() {
    return mapState;
  }

  public StateMachineState getStateMachineState() {
    return stateMachineState;
  }
}
