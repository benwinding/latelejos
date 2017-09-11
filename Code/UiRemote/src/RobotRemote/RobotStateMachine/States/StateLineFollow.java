package RobotRemote.RobotStateMachine.States;

import RobotRemote.RobotStateMachine.IModeBase;
import com.google.common.eventbus.EventBus;

public class StateLineFollow implements IModeBase {
  private StateWaiting state_waiting;
  private StateAutoMapping state_autoMapping;
  private EventBus eventBus;

  public StateLineFollow(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public void linkStates(StateWaiting state_waiting, StateAutoMapping state_autoMapping) {
    this.state_waiting = state_waiting;
    this.state_autoMapping = state_autoMapping;
  }

  public void EnterState() {
    this.eventBus.register(this);
    this.WaitForEvents();
  }

  private void WaitForEvents() {
    // Wait for events
  }

  private void OnFinishedLine() {
    this.eventBus.unregister(this);
    this.state_autoMapping.EnterState();
  }

  private void OnSTOP() {
    this.eventBus.unregister(this);
    this.state_waiting.EnterState();
  }
}
