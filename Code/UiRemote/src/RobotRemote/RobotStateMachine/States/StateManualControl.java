package RobotRemote.RobotStateMachine.States;

import RobotRemote.RobotStateMachine.IModeBase;
import com.google.common.eventbus.EventBus;

public class StateManualControl implements IModeBase {
  private StateWaiting state_waiting;
  private StateManualWarn state_warn;
  private EventBus eventBus;

  public StateManualControl(EventBus eventBus) {

    this.eventBus = eventBus;
  }

  public void linkStates(StateWaiting state_waiting, StateManualWarn state_warn) {
    this.state_waiting = state_waiting;
    this.state_warn = state_warn;
  }

  public void EnterState() {
    // Do stuff
    this.eventBus.register(this);
    this.WaitForEvents();
  }

  private void WaitForEvents() {
    // Wait for events
  }

  private void OnDetectObject() {
    this.eventBus.unregister(this);
    state_warn.EnterState();
  }

  private void OnSTOP() {
    this.eventBus.unregister(this);
    state_waiting.EnterState();
  }
}
