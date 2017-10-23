package RobotRemote.UIServices.MapHandlers;

import RobotRemote.Models.MapPoint;
import RobotRemote.Shared.RobotConfiguration;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserNoGoZoneState {
  private List<List<MapPoint>> allNgzSets;

  public UserNoGoZoneState() {
    allNgzSets = new ArrayList<>();
  }

  public void enableTestNGZ()
  {

    List<MapPoint> testList = new ArrayList<>();
    testList.add(new MapPoint(20.985714900250336,76.33571410665708));
    testList.add(new MapPoint(26.98571500242973,80.47857132006666));
    testList.add(new MapPoint(34.985715138668915,65.47857106461818));
    testList.add(new MapPoint(24.70000067778996,63.04999959468842));
    testList.add(new MapPoint(18.985714866190538,69.1928568421578));
    testList.add(new MapPoint(14.128571926331029,78.47857128600685));
    testList.add(new MapPoint(20.985714900250336,76.33571410665708));
    allNgzSets.add(testList);
  }

  private List<MapPoint> getCurrentNgzPoints() {
    return this.allNgzSets.get(allNgzSets.size()-1);
  }

  public void AddNgzSet(List<MapPoint> ngzSet) {
    this.allNgzSets.add(ngzSet);
    this.AddPointToCurrentList(ngzSet.get(0));
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

  public ArrayList<MapPoint> GetNgzPointsFlattened() {
    ArrayList<MapPoint> returnList = new ArrayList<>();
    for(List<MapPoint> ngzList: allNgzSets) {
      returnList.addAll(ngzList);
    }
    return returnList;
  }

  private void CopyFirstPointToLastPosition(List<MapPoint> ngzCopy) {
    if(ngzCopy != null && ngzCopy.size() > 0)
      ngzCopy.add(ngzCopy.get(0));
  }

  void FinishedNgzSet() {
    if(this.allNgzSets.size() > 0 && this.getCurrentNgzPoints() != null)
      this.CopyFirstPointToLastPosition(this.getCurrentNgzPoints());
  }


  public boolean isRobotInNgz(MapPoint robotLocation, RobotConfiguration config) {
    for(List<MapPoint> ngzSet: this.allNgzSets) {
      if(NgzUtils.isRobotInNgzArea(robotLocation, ngzSet, config.robotPhysicalLength,config.robotPhysicalWidth))
        return true;
    }
    return false;
  }

  public ArrayList<MapPoint> GetObstacles() {
    ArrayList<MapPoint> returnList = new ArrayList<>();
    // TODO implement get obstacle points
    return returnList;
  }
}
