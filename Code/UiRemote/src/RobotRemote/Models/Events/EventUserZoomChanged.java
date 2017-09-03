package RobotRemote.Models.Events;

public class EventUserZoomChanged {
  private boolean incrementIn;

  public EventUserZoomChanged(boolean incrementIn) {
    this.incrementIn = incrementIn;
  }

  public boolean isIncrementIn() {
    return incrementIn;
  }
}
