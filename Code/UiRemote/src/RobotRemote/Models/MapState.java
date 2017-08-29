package RobotRemote.Models;

import java.util.ArrayList;
import java.util.List;

public class MapState {
  private List<MapPoint> pointsVisited = new ArrayList<>();
  private List<MapPoint> pointsMapBorder = new ArrayList<>();
  private float a1_W = 594/2;
  private float a1_L = 841/2;
  private MapPoint mapSize = new MapPoint(a1_W, a1_L);

  public MapState(double xInit, double yInit, float initTheta) {
    pointsVisited.add(new MapPoint(xInit,yInit,initTheta));

    // A1 = 594 x 841 mm
    pointsMapBorder.add(new MapPoint(0,0));
    pointsMapBorder.add(new MapPoint(mapSize.x,0));
    pointsMapBorder.add(new MapPoint(mapSize.x,mapSize.y));
    pointsMapBorder.add(new MapPoint(0,mapSize.y));
    pointsMapBorder.add(new MapPoint(0,0));
  }

  public void AddPoint(double x, double y, double theta) {
    MapPoint newPoint = new MapPoint(y, x, theta); // Reversed x and y for Ui
    this.pointsVisited.add(newPoint);
  }

  public List<MapPoint> GetPointsVisited() {
    return this.pointsVisited;
  }
  public List<MapPoint> GetPointsBorder() { return this.pointsMapBorder; }

  public MapPoint GetMapSize() { return this.mapSize; }

  public MapPoint GetLastPoint() {
    int size = this.pointsVisited.size();
    return this.pointsVisited.get(size - 1);
  }
}

