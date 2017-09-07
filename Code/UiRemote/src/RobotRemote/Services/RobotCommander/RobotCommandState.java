package RobotRemote.Services.RobotCommander;

import RobotRemote.Models.Enums.EnumOperationMode;

public class RobotCommandState {
  private EnumOperationMode currentState;

  public EnumOperationMode getCurrentState() {
    return currentState;
  }

  void setCurrentState(EnumOperationMode currentState) {
    this.currentState = currentState;
  }
}
