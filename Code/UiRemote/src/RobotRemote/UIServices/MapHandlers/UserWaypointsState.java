package RobotRemote.UIServices.MapHandlers;

import RobotRemote.Models.MapPoint;
import lejos.robotics.navigation.Waypoint;

import java.util.ArrayList;
import java.util.List;

public class UserWaypointsState {
  List<Waypoint> waypoints;

  public UserWaypointsState() {
    waypoints = new ArrayList<>();
  }

  public List<Waypoint> GetSelectedWayPoints() {
    return waypoints;
  }

  public List<MapPoint> GetSelectedMapPoints() {
    List<MapPoint> temp = new ArrayList<>();
    for(Waypoint wp: waypoints) {
      temp.add(new MapPoint(wp.x,wp.y));
    }
    return temp;
  }

  void AddWayPoint(double x, double y) {
    this.waypoints.add(new Waypoint(x,y));
  }
}
