package RobotRemote.RobotStateMachine;

import RobotRemote.RobotStateMachine.States.AutoSurveyZigZag;
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
    AutoSurveyZigZag state_autoSurveyZigZag = new AutoSurveyZigZag(sm);

    // Link states with references
    state_manualStopped.linkStates(state_manualMoving, state_autoSurveyZigZag);
    state_manualMoving.linkStates(state_manualStopped);
    state_autoSurveyZigZag.linkStates(state_manualStopped);

    state_manualStopped.EnterState();
  }

  @Subscribe
  private void OnEverything(Object event) {
    Logger.log("EVENT:: " + event.getClass().getSimpleName());
  }
}
