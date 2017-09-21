package RobotRemote.RobotStateMachine.Events;

import RobotRemote.Models.Enums.EnumOperationMode;

public class EventChangeRobotCommand {
  private EnumOperationMode enumOperationMode;

  public EventChangeRobotCommand(EnumOperationMode enumOperationMode) {
    this.enumOperationMode = enumOperationMode;
  }

  public EnumOperationMode getOperationMode() {
    return enumOperationMode;
  }
}
