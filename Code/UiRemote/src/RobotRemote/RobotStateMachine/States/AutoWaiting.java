package RobotRemote.RobotStateMachine.States;

import RobotRemote.RobotStateMachine.Events.EventUserStartAuto;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Services.ServiceLocator;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class AutoWaiting implements IModeState {
  private EventBus eventBus;
  private ServiceLocator serviceLocator;

  private ManualStopped state_stopped;
  private AutoSurveyZigZag state_autoSurveyZigZag;

  public AutoWaiting(EventBus eventBus, ServiceLocator serviceLocator) {
    this.eventBus = eventBus;
    this.serviceLocator = serviceLocator;
  }

  public void linkStates(ManualStopped state_stopped, AutoSurveyZigZag state_autoSurveyZigZag) {
    this.state_stopped = state_stopped;
    this.state_autoSurveyZigZag = state_autoSurveyZigZag;
  }

  @Override
  public void EnterState() {
    this.eventBus.register(this);
  }

  @Subscribe
  public void OnEventUserStartAuto(EventUserStartAuto event) {
    this.eventBus.unregister(this);
    this.state_autoSurveyZigZag.EnterState();
  }
}
