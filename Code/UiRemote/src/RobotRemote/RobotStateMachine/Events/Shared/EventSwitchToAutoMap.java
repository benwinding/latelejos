package RobotRemote.RobotStateMachine.Events.Shared;

import RobotRemote.Models.MapPoint;

public class EventSwitchToAutoMap {
  public  MapPoint waypoint;
  public EventSwitchToAutoMap(){
    this.waypoint=null;
  }

  public EventSwitchToAutoMap(MapPoint waypoint)
  {
    this.waypoint = waypoint;
  }
}
