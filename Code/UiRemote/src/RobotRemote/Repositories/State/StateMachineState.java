package RobotRemote.Repositories.State;

import RobotRemote.Models.StateMachineEnum;

public class StateMachineState {
  private StateMachineEnum currentState;

  public StateMachineEnum getCurrentState() {
    return currentState;
  }

  public void setCurrentState(StateMachineEnum currentState) {
    this.currentState = currentState;
  }
}
