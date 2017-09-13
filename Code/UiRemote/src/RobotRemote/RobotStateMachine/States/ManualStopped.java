package RobotRemote.RobotStateMachine.States;

import RobotRemote.Shared.Logger;
import RobotRemote.RobotStateMachine.Events.EventSwitchToAutoMap;
import RobotRemote.RobotStateMachine.Events.EventSwitchToManual;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.UIServices.ServiceLocator;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class ManualStopped implements IModeState {
  private EventBus eventBus;
  private ServiceLocator serviceLocator;

  private ManualMoving state_manualmoving;
  private AutoWaiting state_autoWaiting;

  public ManualStopped(EventBus eventBus, ServiceLocator serviceLocator) {
    this.eventBus = eventBus;
    this.serviceLocator = serviceLocator;
  }

  public void linkStates(ManualMoving state_manualmoving, AutoWaiting state_autoWaiting) {
    this.state_manualmoving = state_manualmoving;
    this.state_autoWaiting = state_autoWaiting;
  }

  public void EnterState() {
    Logger.log("STATE: ManualStopped...");
    this.eventBus.register(this);
  }

  @Subscribe
  private void OnSwitchToManual(EventSwitchToManual event) {
    this.eventBus.unregister(this);
    this.state_manualmoving.EnterState();
  }

  @Subscribe
  private void OnSwitchToAutoMap(EventSwitchToAutoMap event) {
    this.eventBus.unregister(this);
    this.state_autoWaiting.EnterState();
  }
}
