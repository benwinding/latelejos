package RobotRemote.Services.Listeners.StateMachine;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.Events.EventChangeOperationMode;

public class StateMachineListener{
  private ModeObjectAvoidance modeObjectAvoidance;
  private ModeAutoMapping modeAutomapping;
  private StateMachineState stateMachineState;

  public StateMachineListener(StateMachineState stateMachineState) {
    this.stateMachineState = stateMachineState;
    this.modeAutomapping = new ModeAutoMapping();
    this.modeObjectAvoidance = new ModeObjectAvoidance();
  }

  public void OnChangeMode(EventChangeOperationMode event) {
    Logger.LogCrossThread("Received EventChangeOperationMode: " + event.getOperationMode());
    stateMachineState.setCurrentState(event.getOperationMode());
    switch (event.getOperationMode()) {
      case ManualMode:
        this.modeObjectAvoidance.kill();
        this.modeAutomapping.kill();
        break;
      case AutoMode:
        this.modeAutomapping.start();
        break;
      case AvoidanceMode:
        this.modeAutomapping.kill();
        this.modeObjectAvoidance.start();
        break;
      case Waiting:
        this.modeAutomapping.kill();
        this.modeObjectAvoidance.start();
      default:
        break;
    }
  }
}
