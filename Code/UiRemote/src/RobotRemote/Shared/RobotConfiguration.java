package RobotRemote.Shared;
import javafx.scene.paint.Color;

public class RobotConfiguration {
  public  boolean enableTestData =false;

  // Map Size: A1 594mm x 841mm
  public float mapW = 84.1f; // cm
  public float mapH = 59.4f; // cm
  // Initial map values
  public float mapInitZoom = 0.7f;
  public float mapPixelsPerCm = 10; // Define pixel density of map
  public float initX = 76; // cm
  public float initY = 52; // cm
  public float initTheta = 180; // degrees
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
  public int robotPhysicalWidth = 14;
  public int robotPhysicalLength = 20;
  // Update intervals
  public int updateIntervalMoving_ms = 50;
  public int updateIntervalUi_ms = 50;
  public Color colorTrail = Color.YELLOW;
  public Color colorBorder = Color.BLUE;
  public Color colorCrater = Color.BLACK;
  public Color colorApollo = Color.RED;
  public Color colorRadiation = Color.GREEN;
  public double obstacleAvoidDistance = 8;
  public float zigzagWidth = 10;
}
