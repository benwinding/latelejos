package RobotRemote.Models;

public class RobotConfiguration {
  public float initX = 100;
  public float initY = 200;
  public float initTheta = -90;
  public int ngzRows = 10;
  public int ngzCols = 10;
  public float mapInitZoom = 1;
  public float mapW = 300;
  public float mapH = 500;
  public double robotLinearSpeed_cms = 10;
  public double robotLinearAcceleration_cms2 = 10;
  public double robotAngularSpeed_degs = 30;
  public double robotAngularAcceleration_degs2 = 10;
  public float robotWheelDia = 2.1f;
  public float robotTrackWidth = 4.4f;
  public String wheelPortLeft = "A";
  public String wheelPortRight = "B";
  public int updateIntervalMoving_ms = 50;
  public int updateIntervalUi_ms = 40;
}
