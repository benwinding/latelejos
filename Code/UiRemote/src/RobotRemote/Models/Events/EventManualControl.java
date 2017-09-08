package RobotRemote.Models.Events;

import RobotRemote.Models.Enums.EnumCommandManual;

public class EventManualControl {
  private EnumCommandManual command;

  public EventManualControl(EnumCommandManual command) {
    this.command = command;
  }

  public EnumCommandManual getCommand() {
    return command;
  }
}
