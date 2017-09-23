package RobotRemote.RobotStateMachine.Events.ManualState;

import RobotRemote.Models.Enums.EnumCommandManual;

public class EventManualCommand {
  private EnumCommandManual command;

  public EventManualCommand(EnumCommandManual command) {
    this.command = command;
  }

  public EnumCommandManual getCommand() {
    return command;
  }
}
