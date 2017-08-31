package RobotRemote.Models.Events;

import RobotRemote.Models.EnumOperationMode;

public class EventChangeOperationMode {
  private EnumOperationMode enumOperationMode;

  EventChangeOperationMode(EnumOperationMode enumOperationMode) {
    this.enumOperationMode = enumOperationMode;
  }

  public EnumOperationMode getOperationMode() {
    return enumOperationMode;
  }
}
