package RobotRemote.UIServices.Events;

import RobotRemote.Models.Enums.EnumZoomCommand;

public class EventUserZoomChanged {
  private EnumZoomCommand zoomCommand;

  public EventUserZoomChanged(boolean zoomIn) {
    if(zoomIn)
      this.zoomCommand = EnumZoomCommand.IncrementZoom;
    else
      this.zoomCommand = EnumZoomCommand.DecrementZoom;
  }

  public EventUserZoomChanged(EnumZoomCommand zoomCommand) {
    this.zoomCommand = zoomCommand;
  }

  public EnumZoomCommand getZoomCommand() {
    return zoomCommand;
  }
}
