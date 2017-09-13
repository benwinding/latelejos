package RobotRemote.RobotStateMachine;

import RobotRemote.Shared.Logger;
import RobotRemote.Shared.AppStateRepository;
import RobotRemote.RobotStateMachine.States.*;
import RobotRemote.UIServices.ServiceLocator;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class StateMachineBuilder {
  public StateMachineBuilder(EventBus eventBus, ServiceLocator serviceLocator, AppStateRepository appStateRepository) {
    eventBus.register(this);
    // Create State Instances
    ManualStopped state_stopped = new ManualStopped(eventBus, serviceLocator);
    ManualMoving state_manual = new ManualMoving(eventBus, serviceLocator, appStateRepository);
    AutoWaiting state_autowaiting = new AutoWaiting(eventBus, serviceLocator);
    AutoSurveyZigZag state_autoSurveyZigZag = new AutoSurveyZigZag(eventBus, serviceLocator);

    // Link states with references
    state_stopped.linkStates(state_manual, state_autowaiting);
    state_manual.linkStates(state_stopped);
    state_autowaiting.linkStates(state_stopped, state_autoSurveyZigZag);
    state_autoSurveyZigZag.linkStates(state_autowaiting);

    state_stopped.EnterState();
  }

  @Subscribe
  private void OnEverything(Object event) {
    Logger.log("EVENT:: " + event.getClass().getSimpleName());
  }
}
