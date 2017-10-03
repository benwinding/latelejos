package RobotRemote.Shared;
import javafx.scene.paint.Color;

public class RobotConfiguration {
  // Map Size: A1 594mm x 841mm
  public float mapW = 59.4f; // cm
  public float mapH = 84.1f; // cm
  // Initial map values
  public float mapInitZoom = 0.7f;
  public float mapPixelsPerCm = 10; // Define pixel density of map
  public float initX = 52; // cm
  public float initY = 76; // cm
  public float initTheta = -180; // degrees
  // Robot settings
  public double robotLinearSpeed_cms = 3;
  public double robotLinearAcceleration_cms2 = 0;
  public double robotAngularSpeed_degs = 30;
  public double robotAngularAcceleration_degs2 = 10;
  public String wheelPortLeft = "B";
  public String wheelPortRight = "A";
  public String sensorPortUltra = "S3";
  public String sensorPortColour = "S4";
  // Robot physical attributes
  public float robotWheelDia = 5.6f;  // Tested on hard surface, Will not be accurate on carpet
  public float robotTrackWidth = 10.75f; // Tested on hard surface, Will not be accurate on carpet
  // Update intervals
  public int updateIntervalMoving_ms = 50;
  public int updateIntervalUi_ms = 50;
  public Color colorTrail = Color.RED;
  public Color colorBorder = Color.BLACK;
  public Color colorCrater = Color.RED;
  public Color colorApollo = Color.RED;
  public double obstacleAvoidDistance = 5;
  public float zigzagWidth = 15;
}
