package RobotRemote.UIServices.MapHandlers;

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

  void AddWayPoint(double x, double y) {
    this.waypoints.add(new Waypoint(x,y));
  }
}
