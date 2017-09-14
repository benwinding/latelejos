package RobotRemote.Models;

public class RobotConfiguration {
  public float initX = 30; // cm
  public float initY = 30; // cm
  public float initTheta = -90;
  public int ngzRows = 10;
  public int ngzCols = 15;
  public float mapInitZoom = 1f;
  // A1 594mm x 841mm
  public float mapW = 59.4f; // cm
  public float mapH = 84.1f; // cm
  public float mapPixelsPerCm = 10; // Define pixel density of map
  public double robotLinearSpeed_cms = 3;
  public double robotLinearAcceleration_cms2 = 0;
  public double robotAngularSpeed_degs = 30;
  public double robotAngularAcceleration_degs2 = 10;
  public float robotWheelDia = 5.6f;  // Tested on hard surface, Will not be accurate on carpet
  public float robotTrackWidth = 12f; // Tested on hard surface, Will not be accurate on carpet
  public String wheelPortLeft = "B";
  public String wheelPortRight = "A";
  public int updateIntervalMoving_ms = 50;
  public int updateIntervalUi_ms = 50;
  public String sensorPortUltra = "S3";
  public String sensorPortColour = "S4";
  public String sensorPortTouch = "S1";
}
