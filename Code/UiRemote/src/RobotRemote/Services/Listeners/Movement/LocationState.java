package RobotRemote.Services.Listeners.Movement;

import RobotRemote.Models.MapPoint;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;

import java.util.ArrayList;
import java.util.List;

public class LocationState {
  private List<MapPoint> pointsVisited;

  public LocationState(double xInit, double yInit, float initTheta) {
    pointsVisited = new ArrayList<>();
    pointsVisited.add(new MapPoint(xInit,yInit,initTheta));
  }

  public List<MapPoint> GetPointsVisited() {
    return this.pointsVisited;
  }

  public MapPoint GetCurrentPosition() {
    int size = this.pointsVisited.size();
    return this.pointsVisited.get(size - 1);
  }

  public void GoingToPose(Pose pose) {
    MapPoint newPoint = new MapPoint(pose.getX(), pose.getY(), pose.getHeading());
    this.pointsVisited.add(newPoint);
  }

  public void GoingToWayPoint(Waypoint waypoint) {
    MapPoint newPoint = new MapPoint(waypoint.getX(), waypoint.getY(), waypoint.getHeading());
    this.pointsVisited.add(newPoint);
  }

  void GoingToPoint(double x, double y, double theta) {
    MapPoint newPoint = new MapPoint(x, y, theta);
    this.pointsVisited.add(newPoint);
  }

  void GoingStraight(double distance) {
    MapPoint curr = GetCurrentPosition();
    Pose pose = new Pose();
    pose.setLocation((float) curr.x, (float) curr.y);
    pose.setHeading((float) curr.theta);
    pose.moveUpdate((float) distance);
    GoingToPoint(pose.getX(),pose.getY(),pose.getHeading());
  }

  void ChangingHeading(double angle) {
    MapPoint curr = GetCurrentPosition();
    GoingToPoint(curr.x,curr.y,curr.theta - angle);
  }
}
