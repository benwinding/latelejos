package RobotRemote.Services.Listeners.StateMachine;

import RobotRemote.Models.EnumOperationMode;

public class StateMachineState {
  private EnumOperationMode currentState;

  public EnumOperationMode getCurrentState() {
    return currentState;
  }

  void setCurrentState(EnumOperationMode currentState) {
    this.currentState = currentState;
  }
}
