package RobotRemote.RobotStateMachine.States;

import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Services.ServiceLocator;
import com.google.common.eventbus.EventBus;

public class AutoSurveyZigZag implements IModeState{
  private EventBus eventBus;
  private ServiceLocator serviceLocator;

  private AutoWaiting state_autowaiting;

  public AutoSurveyZigZag(EventBus eventBus, ServiceLocator serviceLocator) {
    this.eventBus = eventBus;
    this.serviceLocator = serviceLocator;
  }

  @Override
  public void EnterState() {
    this.eventBus.register(this);
  }


  public void linkStates(AutoWaiting state_autowaiting) {
    this.state_autowaiting = state_autowaiting;
  }
}
