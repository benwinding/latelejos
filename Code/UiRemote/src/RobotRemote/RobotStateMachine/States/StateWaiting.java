package RobotRemote.RobotStateMachine.States;

import RobotRemote.Helpers.Logger;
import RobotRemote.RobotStateMachine.Events.EventSwitchToAuto;
import RobotRemote.RobotStateMachine.Events.EventSwitchToManual;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Services.ServiceLocator;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class StateWaiting implements IModeState {
  private EventBus eventBus;
  private ServiceLocator serviceLocator;

  private StateManualControl state_manual;
  private StateAutoMapping state_autoMapping;

  public StateWaiting(EventBus eventBus, ServiceLocator serviceLocator) {
    this.eventBus = eventBus;
    this.serviceLocator = serviceLocator;
  }

  public void linkStates(StateManualControl state_manual, StateAutoMapping state_autoMapping) {
    this.state_manual = state_manual;
    this.state_autoMapping = state_autoMapping;
  }

  public void EnterState() {
    Logger.log("STATE: StateWaiting...");
    this.eventBus.register(this);
  }

  @Subscribe
  private void OnSwitchToManual(EventSwitchToManual event) {
    this.eventBus.unregister(this);
    this.state_manual.EnterState();
  }

  @Subscribe
  private void OnSwitchToAutoMapping(EventSwitchToAuto event) {
    this.eventBus.unregister(this);
    this.state_autoMapping.EnterState();
  }
}
