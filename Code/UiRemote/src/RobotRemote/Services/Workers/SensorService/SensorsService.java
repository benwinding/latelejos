package RobotRemote.Services.Workers.SensorService;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Services.Listeners.Connection.RobotConnectionService;
import RobotRemote.Services.RobotWorkerBase;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.remote.ev3.RemoteRequestException;

public class SensorsService extends RobotWorkerBase {
  private RobotConfiguration config;
  private RobotConnectionService connectionService;
  private SensorsState sensorsState;
  private EV3ColorSensor colourSensorConnection;
  private SensorMode colourSensorMode;
  private SensorMode distanceMode;
  private EV3UltrasonicSensor ultrasonicSensorConnection;

  public SensorsService(RobotConfiguration config, RobotConnectionService connectionService, SensorsState sensorsState) {
    super("Sensors Service", 500);
    this.config = config;
    this.connectionService = connectionService;
    this.sensorsState = sensorsState;
  }

  @Override
  protected void OnStart() {
    InitColourSensor();
    InitUltrasonicSensor();
  }

  private void InitUltrasonicSensor() {
    try {
      Port port = this.connectionService.GetBrick().getPort(config.sensorPortUltra);
      this.ultrasonicSensorConnection = new EV3UltrasonicSensor(port);
      this.distanceMode = ultrasonicSensorConnection.getMode("Distance");
    } catch(RemoteRequestException e) {
      Logger.WarnCrossThread("Unable to open ultrasonic sensor, on port: " + config.sensorPortUltra);
    }
  }

  private void InitColourSensor() {
    try {
      Port port = this.connectionService.GetBrick().getPort(config.sensorPortColour);
      this.colourSensorConnection = new EV3ColorSensor(port);
      this.colourSensorMode = colourSensorConnection.getRGBMode();
    } catch(RemoteRequestException e) {
      Logger.WarnCrossThread("Unable to open colour sensor, on port: " + config.sensorPortColour);
    }
  }

  @Override
  protected void Repeat() {
    FetchColourSensor();
    FetchUltrasonicSensor();
  }

  private void FetchUltrasonicSensor() {
    // Set ultrasonic sensor to state
    float[] sample2 = new float[distanceMode.sampleSize()];
    distanceMode.fetchSample(sample2, 0);
    sensorsState.setUltraReading(sample2[0]);
  }

  private void FetchColourSensor() {
    // Set colour sensor to state
    float[] sample=new float[colourSensorMode.sampleSize()];
    colourSensorMode.fetchSample(sample, 0);

    sensorsState.setColourReadingR(sample[0]);
    sensorsState.setColourReadingG(sample[1]);
    sensorsState.setColourReadingB(sample[2]);
  }

  @Override
  protected void OnShutdown() {
    // close all sensor ports
    if(this.colourSensorConnection != null)
      this.colourSensorConnection.close();
    if(this.ultrasonicSensorConnection != null)
      this.ultrasonicSensorConnection.close();
  }
}
