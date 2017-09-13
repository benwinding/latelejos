package RobotRemote.RobotStateMachine.Events;

public class EventWarnOfObject {
  private double ultraDist;

  public EventWarnOfObject(double ultraDist) {
    this.ultraDist = ultraDist;
  }

  public double getUltraDist() {
    return ultraDist;
  }
}

