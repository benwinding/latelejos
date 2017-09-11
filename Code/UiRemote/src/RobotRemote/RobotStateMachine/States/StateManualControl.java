package RobotRemote.RobotStateMachine.States;

import RobotRemote.Helpers.Logger;
import RobotRemote.RobotStateMachine.Events.EventManualDetectedObject;
import RobotRemote.RobotStateMachine.Events.EventSTOP;
import RobotRemote.RobotStateMachine.IModeBase;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

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
    Logger.log("STATE: StateManualControl...");
    this.eventBus.register(this);
  }

  @Subscribe
  private void OnSTOP(EventSTOP event) {
    this.eventBus.unregister(this);
    state_waiting.EnterState();
  }

  @Subscribe
  private void OnDetectObject(EventManualDetectedObject event) {
    this.eventBus.unregister(this);
    state_warn.EnterState();
  }
}
