package RobotRemote.Services.Listeners.StateMachine;

import RobotRemote.Models.StateMachineEnum;

public class StateMachineState {
  private StateMachineEnum currentState;

  public StateMachineEnum getCurrentState() {
    return currentState;
  }

  void setCurrentState(StateMachineEnum currentState) {
    this.currentState = currentState;
  }
}
