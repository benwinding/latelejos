package RobotRemote.RobotServices.Movement;

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

  public MapPoint GetCurrentColourSensorPosition() {
    MapPoint curr = GetCurrentPosition();
    Pose sensorPose = new Pose();
    sensorPose.setLocation((float) curr.x, (float) curr.y);
    sensorPose.setHeading((float) curr.theta);
    // Sensor offset from from of robot
    sensorPose.moveUpdate((float) 3);
    return new MapPoint(sensorPose.getX(), sensorPose.getY(), sensorPose.getHeading());
  }

  public Pose GetCurrentPose() {
    MapPoint current = GetCurrentPosition();
    Pose newPose = new Pose();
    newPose.setLocation((float) current.x, (float) current.y);
    newPose.setHeading((float) current.theta);
    return newPose;
  }

  void GoingToWayPoint(Waypoint waypoint) {
    MapPoint newPoint = new MapPoint(waypoint.getX(), waypoint.getY(), waypoint.getHeading());
    this.pointsVisited.add(newPoint);
  }

  public synchronized void GoingToPose(Pose pose) {
    MapPoint newPoint = new MapPoint(pose.getX(), pose.getY(), pose.getHeading());
    this.pointsVisited.add(newPoint);
  }

  private void GoingToPoint(double x, double y, double theta) {
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
    GoingToPoint(curr.x,curr.y,curr.theta + angle);
  }

  void SetHeading(float degrees) {
    MapPoint curr = GetCurrentPosition();
    GoingToPoint(curr.x,curr.y,degrees);
  }
}
