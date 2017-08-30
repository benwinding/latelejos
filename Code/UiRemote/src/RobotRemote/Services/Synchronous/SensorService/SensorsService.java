package RobotRemote.Services.Synchronous.SensorService;

import RobotRemote.Repositories.State.SensorsState;
import RobotRemote.Services.Synchronous.Connection.RobotConnectionService;
import RobotRemote.Services.Synchronous.RobotThreadBase;

public class SensorsService extends RobotThreadBase{
  private RobotConnectionService connectionService;
  private SensorsState sensorsState;

  public SensorsService(RobotConnectionService connectionService, SensorsState sensorsState) {
    super("Sensors Service", 100);
    this.connectionService = connectionService;
    this.sensorsState = sensorsState;
  }

  private int i;
  @Override
  public void Repeat() {
    sensorsState.setColourReading(i++);
  }
}
