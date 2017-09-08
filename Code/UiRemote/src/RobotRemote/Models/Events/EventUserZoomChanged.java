package RobotRemote.Models.Events;

import RobotRemote.Models.Enums.EnumZoomCommand;

public class EventUserZoomChanged {
  private EnumZoomCommand zoomCommand;

  public EventUserZoomChanged(EnumZoomCommand zoomCommand) {
    this.zoomCommand = zoomCommand;
  }

  public EnumZoomCommand getZoomCommand() {
    return zoomCommand;
  }
}
