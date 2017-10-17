package RobotRemote.UIServices.MapHandlers;

import RobotRemote.Models.MapPoint;
import RobotRemote.Shared.RobotConfiguration;

import java.util.ArrayList;
import java.util.List;

public class UserNoGoZoneState {
  private List<List<MapPoint>> allNgzSets;

  public UserNoGoZoneState() {
    allNgzSets = new ArrayList<>();
  }

  private List<MapPoint> getCurrentNgzPoints() {
    return this.allNgzSets.get(allNgzSets.size()-1);
  }

  public void AddNgzSet(List<MapPoint> ngzSet) {
    this.allNgzSets.add(ngzSet);
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
    for(List<MapPoint> ngzSet: this.allNgzSets) {
      if(NgzUtils.isPointInNgzArea(point, ngzSet))
        return true;
    }
    return false;
  }

  public boolean isRobotInNgz(MapPoint robotLocation, RobotConfiguration config) {
    int robotLong = config.robotPhysicalLength;
    for(List<MapPoint> ngzSet: this.allNgzSets) {
      if(NgzUtils.isRobotInNgzArea(robotLocation, ngzSet, robotLong))
        return true;
    }
    return false;
  }
}
