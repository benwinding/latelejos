package RobotRemote.RobotStateMachine;

import RobotRemote.Models.MapPoint;
import RobotRemote.RobotStateMachine.Events.Shared.EventEmergencySTOP;
import RobotRemote.RobotStateMachine.Events.Shared.EventSwitchToAutoMap;
import RobotRemote.RobotStateMachine.Events.Shared.EventSwitchToManual;
import RobotRemote.RobotStateMachine.States.AutoMode.AutoSurveying;
import RobotRemote.RobotStateMachine.States.IdleState;
import RobotRemote.RobotStateMachine.States.ManualMoving;
import RobotRemote.Shared.Logger;
import RobotRemote.Shared.ServiceManager;
import RobotRemote.UIServices.Events.EventModeChange;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class StateMachineBuilder {
    private IdleState stateIdle;
    private ManualMoving stateManualMoving;
    private AutoSurveying stateAutoSurveying;
    private IModeState currentState;
    private EventBus bus;
    public static  String CurrentMode ="Idle";
    public StateMachineBuilder(ServiceManager sm) {
        this.bus = sm.getEventBus();
        sm.getEventBus().register(this);
        // Create State Instances
        stateManualMoving = new ManualMoving(sm);
        stateAutoSurveying = new AutoSurveying(sm);
        stateIdle = new IdleState(sm);
        stateIdle.Enter();
        currentState = stateIdle;
    }
    public IModeState getCurrentState()
    {
        return  this.currentState;
    }
    private void enterState(IModeState state)
    {
      if(state == this.currentState)
        return;
      this.currentState.Leave();
      this.currentState = state;
      this.currentState.Enter();
    }


    @Subscribe
    private void OnModeChange(EventModeChange event) {
      CurrentMode = event.mode;
    }
    @Subscribe
    private void OnSwitchToManualMode(EventSwitchToManual event) {
        Logger.log("SWITCH TO MANUAL MODE...");
        enterState(stateManualMoving);
        this.bus.post(new EventModeChange("Manual Mode"));
    }

    @Subscribe
    private void OnSwitchToAutoSurveyMode(EventSwitchToAutoMap event) {
        Logger.log("SWITCH TO AUTOSURVEY MODE...");
        if(event.waypoint!=null)
          stateAutoSurveying.setMoveToWaypoint(event.waypoint);

        enterState(stateAutoSurveying);
        this.bus.post(new EventModeChange("Autosurvey Mode"));
    }

    @Subscribe
    private void OnSTOP(EventEmergencySTOP event) {
      Logger.log("EMERGENCY STOP...");
      //Switch to idle mode
      enterState(stateIdle);
      this.bus.post(new EventModeChange("Idle Mode"));
    }

    @Subscribe
    private void OnEveryEvent(Object event) {
        Logger.debug("EVENT:: " + event.getClass().getSimpleName());
    }
}
