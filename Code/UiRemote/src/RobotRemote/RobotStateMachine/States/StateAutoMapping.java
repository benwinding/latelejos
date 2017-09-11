package RobotRemote.RobotStateMachine.States;

import RobotRemote.RobotStateMachine.IModeBase;
import com.google.common.eventbus.EventBus;

public class StateAutoMapping implements IModeBase {
  private StateWaiting state_waiting;
  private StateLineFollow state_lineFollow;
  private StateObjectAvoidance state_objectAvoidance;
  private EventBus eventBus;

  public StateAutoMapping(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public void linkStates(StateWaiting state_waiting, StateLineFollow state_lineFollow, StateObjectAvoidance state_objectAvoidance) {
    this.state_waiting = state_waiting;
    this.state_lineFollow = state_lineFollow;
    this.state_objectAvoidance = state_objectAvoidance;
  }

  public void EnterState() {
    this.eventBus.register(this);
    this.WaitForEvents();
  }

  private void WaitForEvents() {
    // Wait for events
  }

  private void OnSTOP() {
    this.eventBus.unregister(this);
    this.state_waiting.EnterState();
  }

  private void OnDetectTrail() {
    this.eventBus.unregister(this);
    this.state_lineFollow.EnterState();
  }

  private void OnDetectObject() {
    this.eventBus.unregister(this);
    this.state_objectAvoidance.EnterState();
  }
}
