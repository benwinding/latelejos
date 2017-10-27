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

  ArrayList<MapPoint> obstacleList = new ArrayList<>();
  ArrayList<MapPoint> appolloList = new ArrayList<>();

  public ArrayList<MapPoint> GetObstacles() {
    return obstacleList;
  }

  public void AddDetectedObstacle(float x, float y) {
    this.obstacleList.add(new MapPoint(x,y));
  }

  public ArrayList<MapPoint> GetAppollo() {
    return appolloList;
  }

  public void AddDetectedAppollo(float x, float y) {
    this.appolloList.add(new MapPoint(x,y));
  }
}
