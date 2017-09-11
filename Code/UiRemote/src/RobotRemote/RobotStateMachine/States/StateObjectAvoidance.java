package RobotRemote.RobotStateMachine.States;

import RobotRemote.Helpers.Logger;
import RobotRemote.RobotStateMachine.Events.EventFinishedAvoiding;
import RobotRemote.RobotStateMachine.Events.EventSTOP;
import RobotRemote.RobotStateMachine.IModeBase;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class StateObjectAvoidance implements IModeBase {
  private StateWaiting state_waiting;
  private StateAutoMapping state_autoMapping;
  private EventBus eventBus;

  public StateObjectAvoidance(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public void linkStates(StateWaiting state_waiting, StateAutoMapping state_autoMapping) {
    this.state_waiting = state_waiting;
    this.state_autoMapping = state_autoMapping;
  }

  public void EnterState() {
    Logger.log("STATE: StateObjectAvoidance...");
    this.eventBus.register(this);
  }

  @Subscribe
  private void OnSTOP(EventSTOP event) {
    this.eventBus.unregister(this);
    this.state_waiting.EnterState();
  }

  @Subscribe
  private void OnFinishedAvoiding(EventFinishedAvoiding event) {
    this.eventBus.unregister(this);
    this.state_autoMapping.EnterState();
  }
}
