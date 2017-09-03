package RobotRemote.Services.Workers.SensorService;

import RobotRemote.Services.Listeners.Connection.RobotConnectionService;
import RobotRemote.Services.RobotWorkerBase;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;

public class SensorsService extends RobotWorkerBase {
  private RobotConnectionService connectionService;
  private SensorsState sensorsState;
  private EV3ColorSensor colourSensorConnection;
  private SensorMode colourSensorMode;
  private SampleProvider colourProvider;
  private float[] sample;

  public SensorsService(RobotConnectionService connectionService, SensorsState sensorsState) {
    super("Sensors Service", 100);
    this.connectionService = connectionService;
    this.sensorsState = sensorsState;
  }

  private void SetUpSensors() {
    colourSensorConnection = new EV3ColorSensor(SensorPort.S4);
    //colourSensorMode = colourSensorConnection.getRGBMode();
    colourProvider = colourSensorConnection.getRGBMode();
    sample = new float[colourProvider.sampleSize()];
  }

  @Override
  protected void Repeat() {

    colourProvider.fetchSample(sample, 0);
    sensorsState.setColourReadingR(sample[0]*1000);
    sensorsState.setColourReadingG(sample[1]*1000);
    sensorsState.setColourReadingB(sample[2]*1000);

    /*float[] sample=new float[colourSensorMode.sampleSize()];
    colourSensorMode.fetchSample(sample, 0);

    sensorsState.setColourReadingR(sample[0]);
    sensorsState.setColourReadingG(sample[1]);
    sensorsState.setColourReadingB(sample[2]);*/
  }

  @Override
  protected void OnStart() {
    SetUpSensors();
  }

  @Override
  protected void OnShutdown() {
    // close all sensor ports
    colourSensorConnection.close();
  }
}
