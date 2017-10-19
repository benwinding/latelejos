package RobotRemote.Models;

import java.util.Map;

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

  public String ToString(){
    return x+ ","+y;
  }

  public  boolean equals(MapPoint p)
  {
      double error = 1;
      return  Math.abs(x - p.x) <error && Math.abs(y -p.y) <error;
  }
}
