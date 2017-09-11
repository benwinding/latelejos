package RobotRemote.RobotStateMachine.States;

import RobotRemote.Helpers.Logger;
import RobotRemote.RobotStateMachine.Events.EventAutomapDetectedObject;
import RobotRemote.RobotStateMachine.Events.EventDetectedLineToFollow;
import RobotRemote.RobotStateMachine.Events.EventSTOP;
import RobotRemote.RobotStateMachine.IModeBase;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

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
    Logger.log("STATE: StateAutoMapping...");
    this.eventBus.register(this);
  }

  @Subscribe
  private void OnSTOP(EventSTOP event) {
    this.eventBus.unregister(this);
    this.state_waiting.EnterState();
  }

  @Subscribe
  private void OnDetectTrail(EventDetectedLineToFollow event) {
    this.eventBus.unregister(this);
    this.state_lineFollow.EnterState();
  }

  @Subscribe
  private void OnDetectObject(EventAutomapDetectedObject event) {
    this.eventBus.unregister(this);
    this.state_objectAvoidance.EnterState();
  }
}
