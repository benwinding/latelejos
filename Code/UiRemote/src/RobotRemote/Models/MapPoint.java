package RobotRemote.Models;

public class MapPoint {
  public double x;
  public double y;
  public double theta;

  public MapPoint(double x, double y) {
    this.x = x;
    this.y = y;
    this.theta = 0;
  }

  public MapPoint(double x, double y, double theta) {
    this.x = x;
    this.y = y;
    this.theta = theta;
  }
}
