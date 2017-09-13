package RobotRemote.RobotStateMachine.Events;

public class EventWarnOfColour {
  private int colourId;

  public EventWarnOfColour(int colourId) {
    this.colourId = colourId;
  }

  public int getColourId() {
    return colourId;
  }
}
