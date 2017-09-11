package RobotRemote.RobotStateMachine;

import RobotRemote.RobotStateMachine.States.*;
import com.google.common.eventbus.EventBus;

public class StateMachineBuilder {
  public StateMachineBuilder(EventBus eventBus) {
    // Create State Instances
    StateWaiting state_waiting = new StateWaiting(eventBus);
    StateManualControl state_manual = new StateManualControl(eventBus);
    StateAutoMapping state_autoMapping = new StateAutoMapping(eventBus);
    StateObjectAvoidance state_objectAvoidance = new StateObjectAvoidance(eventBus);
    StateManualWarn state_warn = new StateManualWarn(eventBus);
    StateLineFollow state_lineFollow = new StateLineFollow(eventBus);

    // Link states with references
    state_waiting.linkStates(state_manual, state_autoMapping);
    state_manual.linkStates(state_waiting, state_warn);
    state_warn.linkStates(state_manual);
    state_autoMapping.linkStates(state_waiting, state_lineFollow, state_objectAvoidance);
    state_lineFollow.linkStates(state_waiting, state_autoMapping);
    state_objectAvoidance.linkStates(state_waiting, state_autoMapping);

    state_waiting.EnterState();
  }
}
