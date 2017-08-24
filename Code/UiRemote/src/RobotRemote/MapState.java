package RobotRemote;

import java.util.ArrayList;
import java.util.List;

public class MapState {
  private List<MapPoint> pointsVisited= new ArrayList<MapPoint>();

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