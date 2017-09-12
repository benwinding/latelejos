package RobotRemote.RobotStateMachine.States;

import RobotRemote.Helpers.Logger;
import RobotRemote.RobotStateMachine.Events.EventEmergencySTOP;
import RobotRemote.RobotStateMachine.Events.EventWarnOfObject;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Services.ServiceLocator;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class StateManualWarn implements IModeState {
  private StateManualControl state_manual;
  private StateWaiting state_waiting;
  private EventBus eventBus;

  public StateManualWarn(EventBus eventBus, ServiceLocator serviceLocator) {
    this.eventBus = eventBus;
  }

  public void linkStates(StateWaiting state_waiting, StateManualControl state_manual) {
    this.state_manual = state_manual;
    this.state_waiting = state_waiting;
  }

  public void EnterState() {
    Logger.log("STATE: StateManualWarn...");
    this.eventBus.register(this);
  }

  @Subscribe
  private void OnWarningDismissed(EventWarnOfObject event) {
    this.eventBus.unregister(this);
    this.state_manual.EnterState();
  }

  @Subscribe
  private void OnSTOP(EventEmergencySTOP event) {
    this.eventBus.unregister(this);
    this.state_waiting.EnterState();
  }
}
