package RobotRemote.Models;

public class RobotConfiguration {
  public float initX = 100;
  public float initY = 200;
  public float initTheta = -90;
  public int ngzRows = 10;
  public int ngzCols = 15;
  public float mapInitZoom = 1;
  public float mapW = 300;
  public float mapH = 500;
  public double robotLinearSpeed_cms = 5;
  public double robotLinearAcceleration_cms2 = 10;
  public double robotAngularSpeed_degs = 30;
  public double robotAngularAcceleration_degs2 = 10;
  public float robotWheelDia = 5.6f;  // Tested on hard surface, Will not be accurate on carpet
  public float robotTrackWidth = 13f; // Tested on hard surface, Will not be accurate on carpet
  public String wheelPortLeft = "A";
  public String wheelPortRight = "B";
  public int updateIntervalMoving_ms = 100;
  public int updateIntervalUi_ms = 200;
}
