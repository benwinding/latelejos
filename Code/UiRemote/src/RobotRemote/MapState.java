package RobotRemote;

import java.util.ArrayList;
import java.util.List;

public class MapState {
  private List<MapPoint> pointsVisited;

  public MapState(double xInit, double yInit) {
    pointsVisited = new ArrayList<>();
    pointsVisited.add(new MapPoint(xInit,yInit));
  }

  void AddPoint(double x, double y) {
    MapPoint newPoint = new MapPoint(x,y);
    this.pointsVisited.add(newPoint);
  }

  List<MapPoint> GetPointsVisited() {
    return this.pointsVisited;
  }
}

class MapPoint {
  double x;
  double y;

  MapPoint(double x,double y) {
    this.x = x;
    this.y = y;
  }
}