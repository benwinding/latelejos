package RobotRemote.RobotStateMachine.States;

import RobotRemote.RobotStateMachine.IModeBase;
import com.google.common.eventbus.EventBus;

public class StateManualWarn implements IModeBase {
  private StateManualControl state_manual;
  private EventBus eventBus;

  public StateManualWarn(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public void EnterState() {
    this.eventBus.register(this);
    this.WaitForEvents();
  }

  private void WaitForEvents() {
    // Wait for events
  }

  public void linkStates(StateManualControl state_manual) {
    this.state_manual = state_manual;
  }

  private void OnWarningDismissed() {
    this.eventBus.unregister(this);
    this.state_manual.EnterState();
  }
}
