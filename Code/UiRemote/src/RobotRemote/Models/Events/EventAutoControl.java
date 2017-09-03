package RobotRemote.Models.Events;

import lejos.robotics.navigation.Waypoint;

public class EventAutoControl {
  private Waypoint nextWayPoint;

  public EventAutoControl(Waypoint nextWayPoint) {
    this.nextWayPoint = nextWayPoint;
  }

  public Waypoint getNextWayPoint() {
    return nextWayPoint;
  }
}
