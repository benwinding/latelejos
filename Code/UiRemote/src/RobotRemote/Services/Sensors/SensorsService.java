package RobotRemote.Services.Sensors;

import RobotRemote.Helpers.Logger;
import RobotRemote.Helpers.Synchronizer;
import RobotRemote.Models.MapPoint;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.Services.Connection.RobotConnectionService;
import RobotRemote.Services.Movement.LocationState;
import RobotRemote.Services.RobotServiceBase;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;

import java.rmi.RemoteException;

public class SensorsService extends RobotServiceBase {
  private DiscoveredColoursState discoveredColoursState;
  private LocationState locationState;
  private RobotConfiguration config;
  private RobotConnectionService connectionService;
  private SensorsState sensorsState;

  private EV3ColorSensor colourSensorConnection;
  private SensorMode colourSensorMode;

  private EV3UltrasonicSensor ultrasonicConnection;
  private SensorMode ultrasonicMode;

  private RMISampleProvider rmiTouchMode;

  public SensorsService(RobotConfiguration config, RobotConnectionService connectionService, AppStateRepository appStateRepository) {
    super("Sensors Service", 100);
    this.config = config;
    this.connectionService = connectionService;
    this.sensorsState = appStateRepository.getSensorsState();
    this.locationState = appStateRepository.getLocationState();
    this.discoveredColoursState = appStateRepository.getDiscoveredColoursState();
  }

  @Override
  protected void OnStart() {
    Synchronizer.RunNotConcurrent(() -> {
      InitRmiTouchSensor();
      InitColourSensor();
      InitUltrasonicSensor();
    });
  }

  private void InitRmiTouchSensor() {
    try {
      Logger.LogCrossThread("Opening touch sensor, on port: " + config.sensorPortTouch);
      RemoteEV3 ev3 = this.connectionService.GetBrickeRemoteEv3();
      this.rmiTouchMode = ev3.createSampleProvider("S1", "lejos.hardware.sensor.EV3TouchSensor", "Touch");
      Thread.sleep(50);
      Logger.LogCrossThread("Success: opened touch sensor, on port: " + config.sensorPortTouch);
    } catch(Exception e) {
      Logger.WarnCrossThread("Error: Unable to open touch sensor, on port: " + config.sensorPortTouch);
    }
  }

  private void InitUltrasonicSensor() {
    try {
      Logger.LogCrossThread("Opening ultrasonic sensor, on port: " + config.sensorPortUltra);
      Port port = this.connectionService.GetBrick().getPort(config.sensorPortUltra);
      this.ultrasonicConnection = new EV3UltrasonicSensor(port);
      this.ultrasonicMode = ultrasonicConnection.getMode("Distance");
      Thread.sleep(50);
      Logger.LogCrossThread("Success: opened ultrasonic sensor, on port: " + config.sensorPortUltra);
    } catch(Exception e) {
      Logger.WarnCrossThread("Error: Unable to open ultrasonic sensor, on port: " + config.sensorPortUltra);
    }
  }

  private void InitColourSensor() {
    try {
      Logger.LogCrossThread("Opening colour sensor, on port: " + config.sensorPortColour);
      Port port = this.connectionService.GetBrick().getPort(config.sensorPortColour);
      this.colourSensorConnection = new EV3ColorSensor(port);
      this.colourSensorMode = colourSensorConnection.getRGBMode();
      Thread.sleep(50);
      Logger.LogCrossThread("Success: opened colour sensor, on port: " + config.sensorPortColour);
    } catch(Exception e) {
      Logger.WarnCrossThread("Error: Unable to open colour sensor, on port: " + config.sensorPortColour);
    }
  }

  @Override
  protected void Repeat() {
    Synchronizer.RunNotConcurrent(() -> {
      FetchRmiTouchSensor();
      FetchColourSensor();
      FetchUltrasonicSensor();
    });
  }

  private void FetchRmiTouchSensor() {
    if(rmiTouchMode == null)
      return;
    float[] sample;
    try {
      sample = rmiTouchMode.fetchSample();
      sensorsState.setTouchReading(sample[0]);
    } catch (RemoteException e) {
      Logger.WarnCrossThread("Sensor: unable to read touch sensor");
    }
  }

  private void FetchUltrasonicSensor() {
    // Set ultrasonic sensor to state
    if(ultrasonicMode == null)
      return;
    float[] sample2 = new float[ultrasonicMode.sampleSize()];
    ultrasonicMode.fetchSample(sample2, 0);
    sensorsState.setUltraReading(sample2[0]);
  }

  private void FetchColourSensor() {
    // Set colour sensor to state
    if(colourSensorMode == null)
      return;
    float[] sample=new float[colourSensorMode.sampleSize()];
    colourSensorMode.fetchSample(sample, 0);

    sensorsState.setColourReadingR(sample[0]);
    sensorsState.setColourReadingG(sample[1]);
    sensorsState.setColourReadingB(sample[2]);

    int currentColor = colourSensorConnection.getColorID();
    sensorsState.setColourId(currentColor);

    MapPoint currentPosition = locationState.GetCurrentColourSensorPosition();
    discoveredColoursState.AddColouredPoint(currentColor, currentPosition);
  }

  @Override
  protected void OnShutdown() {
    // close all sensor ports
    Synchronizer.RunNotConcurrent(() -> {
      try{
        this.ultrasonicConnection.close();
        Thread.sleep(100);
      } catch (Exception ignored) {
        Logger.WarnCrossThread("Sensor Service, Error closing the ultrasonic sensor port");
      }
      try{
        this.rmiTouchMode.close();
        Thread.sleep(100);
      } catch (Exception ignored) {
        Logger.WarnCrossThread("Sensor Service, Error closing the touch sensor port");
      }
      try{
        this.colourSensorConnection.close();
      } catch (Exception ignored) {
        Logger.WarnCrossThread("Sensor Service, Error closing the colour sensor port");
      }
    });
  }
}
