package RobotRemote.Repositories;

import RobotRemote.Models.MapPoint;

import java.util.ArrayList;
import java.util.List;

public class LocationState {
  private List<MapPoint> pointsVisited;

  public LocationState(double xInit, double yInit, float initTheta) {
    pointsVisited = new ArrayList<>();
    pointsVisited.add(new MapPoint(xInit,yInit,initTheta));
  }

  public void AddPoint(double x, double y, double theta) {
    MapPoint newPoint = new MapPoint(x, y, theta); // Reversed x and y for Ui
    this.pointsVisited.add(newPoint);
  }

  public List<MapPoint> GetPointsVisited() {
    return this.pointsVisited;
  }

  public MapPoint GetLastPoint() {
    int size = this.pointsVisited.size();
    return this.pointsVisited.get(size - 1);
  }
}
