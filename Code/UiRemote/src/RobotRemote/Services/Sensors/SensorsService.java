package RobotRemote.Services.Sensors;

import RobotRemote.Helpers.Logger;
import RobotRemote.Helpers.Synchronizer;
import RobotRemote.Models.MapPoint;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.Services.Connection.RobotConnectionService;
import RobotRemote.Services.Movement.MoveThreads.LocationState;
import RobotRemote.Services.RobotServiceBase;
import com.google.common.eventbus.EventBus;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;
import lejos.remote.ev3.RemoteRequestSampleProvider;
import lejos.robotics.SampleProvider;

import java.rmi.RemoteException;

public class SensorsService extends RobotServiceBase {
  private DiscoveredColoursState discoveredColoursState;
  private LocationState locationState;
  private EventBus eventBus;
  private RobotConfiguration config;
  private RobotConnectionService connectionService;
  private SensorsState sensorsState;

  private EV3ColorSensor colourSensorConnection;
  private SensorMode colourSensorMode;

  private RMISampleProvider ultraSampleProvider;

  public SensorsService(EventBus eventBus, RobotConfiguration config, RobotConnectionService connectionService, AppStateRepository appStateRepository) {
    super("Sensors Service", 50);
    this.eventBus = eventBus;
    this.config = config;
    this.connectionService = connectionService;
    this.sensorsState = appStateRepository.getSensorsState();
    this.locationState = appStateRepository.getLocationState();
    this.discoveredColoursState = appStateRepository.getDiscoveredColoursState();
  }

  @Override
  protected void OnStart() {
    Synchronizer.RunNotConcurrent(() -> {
      InitColourSensor();
      InitUltrasonicSensor();
    });
  }

  private void InitUltrasonicSensor() {
    try {
      Logger.log("Opening ultrasonic sensor, on port: " + config.sensorPortUltra);
      ultraSampleProvider =  this.connectionService.GetBrickeRemoteEv3().createSampleProvider(config.sensorPortUltra,"lejos.hardware.sensor.EV3UltrasonicSensor","Distance");
      Thread.sleep(100);
      sensorsState.setStatusUltra(true);
      Logger.log("Success: opened ultrasonic sensor, on port: " + config.sensorPortUltra);
    } catch(Exception e) {
      Logger.warn("Error: Unable to open ultrasonic sensor, on port: " + config.sensorPortUltra);
    }
  }

  private void InitColourSensor() {
    try {
      Logger.log("Opening colour sensor, on port: " + config.sensorPortColour);
      Port port = this.connectionService.GetBrick().getPort(config.sensorPortColour);
      this.colourSensorConnection = new EV3ColorSensor(port);
      this.colourSensorMode = colourSensorConnection.getRGBMode();
      Thread.sleep(100);
      sensorsState.setStatusColour(true);
      Logger.log("Success: opened colour sensor, on port: " + config.sensorPortColour);
    } catch(Exception e) {
      Logger.warn("Error: Unable to open colour sensor, on port: " + config.sensorPortColour);
    }
  }

  @Override
  protected void Repeat() {
    Synchronizer.RunNotConcurrent(() -> {
      FetchColourSensor();
      FetchUltrasonicSensor();
    });
  }

  private void FetchUltrasonicSensor() {
    // Set ultrasonic sensor to state
    if(ultraSampleProvider==null)
      return;
    try {
      float[] sample = ultraSampleProvider.fetchSample();
      sensorsState.setUltraReading(sample[0]);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
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
        this.ultraSampleProvider.close();
        Thread.sleep(200);
      } catch (Exception ignored) {
        Logger.warn("Sensor Service, Error closing the ultrasonic sensor port");
      }
      try{
        this.colourSensorConnection.close();
        Thread.sleep(200);
      } catch (Exception ignored) {
        Logger.warn("Sensor Service, Error closing the colour sensor port");
      }
    });
  }
}
