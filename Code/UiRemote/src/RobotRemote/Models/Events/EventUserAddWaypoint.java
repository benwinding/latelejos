package RobotRemote.Models.Events;

import lejos.robotics.navigation.Waypoint;

public class EventUserAddWaypoint {
  private double mapLocationX;
  private double mapLocationY;

  public EventUserAddWaypoint(Waypoint gotoOnMap) {
    this.mapLocationX = gotoOnMap.getX();
    this.mapLocationY = gotoOnMap.getY();
  }

  public double getX() {
    return mapLocationX;
  }

  public double getY() {
    return mapLocationY;
  }
}
