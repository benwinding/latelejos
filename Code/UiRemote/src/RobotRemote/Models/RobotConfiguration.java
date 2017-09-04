package RobotRemote.Models;

public class RobotConfiguration {
  public float initX = 100;
  public float initY = 200;
  public float initTheta = 90;
  public int ngzRows = 10;
  public int ngzCols = 15;
  public float mapInitZoom = 1f;
  public float mapW = 594; // A1 594mm x 841mm
  public float mapH = 841;
  public double robotLinearSpeed_cms = 5;
  public double robotLinearAcceleration_cms2 = 0;
  public double robotAngularSpeed_degs = 30;
  public double robotAngularAcceleration_degs2 = 10;
  public float robotWheelDia = 5.6f;  // Tested on hard surface, Will not be accurate on carpet
  public float robotTrackWidth = 13f; // Tested on hard surface, Will not be accurate on carpet
  public String wheelPortLeft = "A";
  public String wheelPortRight = "B";
  public int updateIntervalMoving_ms = 100;
  public int updateIntervalUi_ms = 200;
  public String sensorPortUltra = "S3";
  public String sensorPortColour = "S4";
}
