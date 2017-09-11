package RobotRemote.RobotStateMachine.States;

import RobotRemote.RobotStateMachine.IModeBase;
import com.google.common.eventbus.EventBus;

public class StateWaiting implements IModeBase {
  private StateManualControl state_manual;
  private StateAutoMapping state_autoMapping;
  private EventBus eventBus;

  public StateWaiting(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public void linkStates(StateManualControl state_manual, StateAutoMapping state_autoMapping) {
    this.state_manual = state_manual;
    this.state_autoMapping = state_autoMapping;
  }

  public void EnterState() {
    this.eventBus.register(this);
    this.WaitForEvents();
  }

  private void WaitForEvents() {
    // Wait for events
  }

  private void OnSwitchToManual() {
    this.eventBus.unregister(this);
    this.state_manual.EnterState();
  }

  private void OnSwitchToAutoMapping() {
    this.eventBus.unregister(this);
    this.state_autoMapping.EnterState();
  }
}
