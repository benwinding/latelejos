package RobotRemote.RobotStateMachine;

import RobotRemote.RobotStateMachine.States.AutoSurveying;
import RobotRemote.RobotStateMachine.States.ManualMoving;
import RobotRemote.RobotStateMachine.States.ManualStopped;
import RobotRemote.Shared.Logger;
import RobotRemote.Shared.ServiceManager;
import com.google.common.eventbus.Subscribe;

public class StateMachineBuilder {
  public StateMachineBuilder(ServiceManager sm) {
    sm.getEventBus().register(this);
    // Create State Instances
    ManualStopped state_manualStopped = new ManualStopped(sm);
    ManualMoving state_manualMoving = new ManualMoving(sm);
    AutoSurveying state_autoSurveying = new AutoSurveying(sm);

    // Link states with references
    state_manualStopped.linkStates(state_manualMoving, state_autoSurveying);
    state_manualMoving.linkStates(state_manualStopped);
    state_autoSurveying.linkStates(state_manualStopped);

    state_manualStopped.EnterState();
  }

  @Subscribe
  private void OnEveryEvent(Object event) {
    Logger.log("EVENT:: " + event.getClass().getSimpleName());
  }
}
