package RobotRemote.RobotStateMachine.Events;

import javafx.scene.paint.Color;

public class EventWarnOfColour {
  private Color colourEnum;

  public EventWarnOfColour(Color colourEnum) {
    this.colourEnum = colourEnum;
  }

  public Color getColourEnum() {
    return colourEnum;
  }
}
