package RobotRemote.UIServices.MapHandlers;

import RobotRemote.Models.MapPoint;
import RobotRemote.Shared.RobotConfiguration;
import lejos.utility.Matrix;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserNoGoZoneState {
  private List<List<MapPoint>> allNgzSets;

  public UserNoGoZoneState() {
    allNgzSets = new ArrayList<>();
  }

  private List<MapPoint> getCurrentNgzPoints() {
    return this.allNgzSets.get(allNgzSets.size()-1);
  }

  void AddNgzStartPoint(MapPoint newNgzPoint) {
    this.allNgzSets.add(new ArrayList<>());
    AddPointToCurrentList(newNgzPoint);
  }

  void AddNgzMiddlePoint(MapPoint newNgzPoint) {
    AddPointToCurrentList(newNgzPoint);
  }

  private void AddPointToCurrentList(MapPoint newNgzPoint) {
    this.getCurrentNgzPoints().add(newNgzPoint);
  }

  public List<List<MapPoint>> GetNgzPoints() {
    if(this.allNgzSets.size() > 0 && this.getCurrentNgzPoints() != null)
      return this.allNgzSets;
    else
      return new ArrayList<>();
  }

  private void CopyFirstPointToLastPosition(List<MapPoint> ngzCopy) {
    if(ngzCopy != null && ngzCopy.size() > 0)
      ngzCopy.add(ngzCopy.get(0));
  }

  void FinishedNgzSet() {
    if(this.allNgzSets.size() > 0 && this.getCurrentNgzPoints() != null)
      this.CopyFirstPointToLastPosition(this.getCurrentNgzPoints());
  }

  public boolean isPointInNgz(MapPoint point) {
    Point testPoint = new Point((int)point.x,(int)point.y);
    for(List<MapPoint> ngzSet: this.allNgzSets) {
      Polygon polygon = new Polygon();
      for(MapPoint mapPoint: ngzSet) {
        polygon.addPoint((int)mapPoint.x,(int)mapPoint.y);
      }
      if(polygon.contains(testPoint))
        return true;
    }
    return false;
  }

  public boolean isRobotInNgz(MapPoint robotLocation, RobotConfiguration config) {
    int robotLong = config.robotPhysicalLength;
    int x = (int)robotLocation.x - robotLong / 2;
    int y = (int)robotLocation.y - robotLong / 2;
    Rectangle2D testPoint = new Rectangle(x,y,robotLong,robotLong);
    for(List<MapPoint> ngzSet: this.allNgzSets) {
      Polygon polygon = new Polygon();
      for(MapPoint mapPoint: ngzSet) {
        polygon.addPoint((int)mapPoint.x,(int)mapPoint.y);
      }
      if(polygon.intersects(testPoint))
        return true;
      if(polygon.contains(testPoint))
        return true;
    }
    return false;
  }
}

