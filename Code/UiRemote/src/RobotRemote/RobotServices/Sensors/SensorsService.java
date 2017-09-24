package RobotRemote.RobotServices.Sensors;

import RobotRemote.Shared.Logger;
import RobotRemote.Shared.Synchronizer;
import RobotRemote.Models.MapPoint;
import RobotRemote.Shared.RobotConfiguration;
import RobotRemote.Shared.AppStateRepository;
import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.RobotServices.Movement.LocationState;
import RobotRemote.Shared.ServiceBase;
import com.google.common.eventbus.EventBus;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.remote.ev3.RMISampleProvider;

import java.rmi.RemoteException;

public class SensorsService extends ServiceBase {
  private DiscoveredColoursState discoveredColoursState;
  private EventBus eventBus;
  private LocationState locationState;
  private RobotConfiguration config;
  private RobotConnectionService connectionService;
  private SensorsState sensorsState;

  private EV3ColorSensor colourSensorConnection;
  private SensorMode colourSensorMode;

  private RMISampleProvider ultraSampleProvider;

  public SensorsService(RobotConfiguration config, RobotConnectionService connectionService, AppStateRepository appStateRepository, EventBus eventBus) {
    super("Sensors Service", 20);
    this.config = config;
    this.connectionService = connectionService;
    this.sensorsState = appStateRepository.getSensorsState();
    this.locationState = appStateRepository.getLocationState();
    this.discoveredColoursState = appStateRepository.getDiscoveredColoursState();
    this.eventBus = eventBus;
  }

  @Override
  protected void OnStart() {
    Synchronizer.SerializeRobotCalls(() -> {
      InitColourSensor();
      InitUltrasonicSensor();
    });
  }

  private void InitUltrasonicSensor() {
    try {
      Logger.debug("Opening ultrasonic sensor, on port: " + config.sensorPortUltra);
      ultraSampleProvider =  this.connectionService.GetBrickeRemoteEv3().createSampleProvider(config.sensorPortUltra,"lejos.hardware.sensor.EV3UltrasonicSensor","Distance");
      Thread.sleep(100);
      sensorsState.setStatusUltra(true);
      Logger.debug("Success: opened ultrasonic sensor, on port: " + config.sensorPortUltra);
    } catch(Exception e) {
      Logger.warn("Error: Unable to open ultrasonic sensor, on port: " + config.sensorPortUltra);
    }
  }

  private void InitColourSensor() {
    try {
      Logger.debug("Opening colour sensor, on port: " + config.sensorPortColour);
      Port port = this.connectionService.GetBrick().getPort(config.sensorPortColour);
      this.colourSensorConnection = new EV3ColorSensor(port);
      this.colourSensorMode = colourSensorConnection.getRGBMode();
      Thread.sleep(100);
      sensorsState.setStatusColour(true);
      Logger.debug("Success: opened colour sensor, on port: " + config.sensorPortColour);
    } catch(Exception e) {
      Logger.warn("Error: Unable to open colour sensor, on port: " + config.sensorPortColour);
    }
  }

  @Override
  protected void Repeat() {
    Synchronizer.SerializeRobotCalls(() -> {
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
    Synchronizer.SerializeRobotCalls(() -> {
      try{
        Thread.sleep(200);
        this.ultraSampleProvider.close();
        Thread.sleep(200);
      } catch (Exception ignored) {
        Logger.warn("Sensor Service, Error closing the ultrasonic sensor port");
      }
      try{
        Thread.sleep(200);
        this.colourSensorConnection.close();
        Thread.sleep(200);
      } catch (Exception ignored) {
        Logger.warn("Sensor Service, Error closing the colour sensor port");
      }
    });
  }
}
