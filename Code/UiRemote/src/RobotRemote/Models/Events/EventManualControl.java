package RobotRemote.Models.Events;

import RobotRemote.Models.EnumCommandManual;

public class EventManualControl {
  private EnumCommandManual command;

  public EventManualControl(EnumCommandManual command) {
    this.command = command;
  }

  public EnumCommandManual getCommand() {
    return command;
  }
}
