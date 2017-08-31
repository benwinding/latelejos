package RobotRemote.Services.Synchronous.SensorService;

import RobotRemote.Repositories.State.SensorsState;
import RobotRemote.Services.Synchronous.Connection.RobotConnectionService;
import RobotRemote.Services.RobotServiceBase;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;

public class SensorsService extends RobotServiceBase {
  private RobotConnectionService connectionService;
  private SensorsState sensorsState;
  private EV3ColorSensor colourSensorConnection;
  private SensorMode colourSensorMode;

  public SensorsService(RobotConnectionService connectionService, SensorsState sensorsState) {
    super("Sensors Service", 100);
    this.connectionService = connectionService;
    this.sensorsState = sensorsState;
    SetUpSensors();
  }

  private void SetUpSensors() {
    colourSensorConnection = new EV3ColorSensor(SensorPort.S4);
    colourSensorMode = colourSensorConnection.getRGBMode();
  }

  @Override
  protected void Repeat() {
    float[] sample=new float[colourSensorMode.sampleSize()];
    colourSensorMode.fetchSample(sample, 0);

    sensorsState.setColourReadingR(sample[0]);
    sensorsState.setColourReadingG(sample[1]);
    sensorsState.setColourReadingB(sample[2]);
  }

  @Override
  protected void OnShutdown() {
    // close all sensor ports
    colourSensorConnection.close();
  }
}
