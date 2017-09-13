package RobotRemote.RobotStateMachine.States;

import RobotRemote.Shared.Logger;
import RobotRemote.RobotStateMachine.Events.EventSwitchToAutoMap;
import RobotRemote.RobotStateMachine.Events.EventSwitchToManual;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Shared.ServiceManager;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class ManualStopped implements IModeState {
  private EventBus eventBus;

  private ManualMoving state_manualmoving;
  private AutoSurveyZigZag state_autoSurveyZigZag;

  public ManualStopped(ServiceManager sm) {
    this.eventBus = sm.getEventBus();
  }

  public void linkStates(ManualMoving state_manualmoving, AutoSurveyZigZag state_autoSurveyZigZag) {
    this.state_manualmoving = state_manualmoving;
    this.state_autoSurveyZigZag = state_autoSurveyZigZag;
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
    this.state_autoSurveyZigZag.EnterState();
  }
}
