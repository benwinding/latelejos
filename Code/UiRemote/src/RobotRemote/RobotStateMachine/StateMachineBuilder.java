package RobotRemote.RobotStateMachine;

import RobotRemote.Models.Enums.EnumCommandManual;
import RobotRemote.RobotStateMachine.Events.ManualState.EventManualCommand;
import RobotRemote.RobotStateMachine.Events.Shared.EventEmergencySTOP;
import RobotRemote.RobotStateMachine.Events.Shared.EventSwitchToAutoMap;
import RobotRemote.RobotStateMachine.Events.Shared.EventSwitchToManual;
import RobotRemote.RobotStateMachine.States.AutoSurveying;
import RobotRemote.RobotStateMachine.States.ManualMoving;
import RobotRemote.RobotStateMachine.States.ManualStopped;
import RobotRemote.Shared.Logger;
import RobotRemote.Shared.ServiceManager;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class StateMachineBuilder {
    private EventBus eventBus;
    ServiceManager sm;
    ManualStopped state_idle;
    ManualMoving state_manualMoving;
    AutoSurveying state_autoSurveying;
    public StateMachineBuilder(ServiceManager sm) {
        sm.getEventBus().register(this);
        this.eventBus = sm.getEventBus();
        this.sm =sm;

        // Create State Instances
        state_manualMoving = new ManualMoving(sm);
        state_autoSurveying = new AutoSurveying(sm);
        state_idle = new ManualStopped(sm);
        state_idle.Enter();
  }

//    @Subscribe
//    private void OnEveryEvent(Object event) {
//        Logger.log("EVENT:: " + event.getClass().getSimpleName());
//  }


    @Subscribe
    private void OnSwitchToManualMode(EventSwitchToManual event)
    {
        Logger.log("SWITCH TO MANUAL MODE...");
        this.state_autoSurveying.Leave();
        this.state_manualMoving.Enter();
    }

    @Subscribe
    private void OnSwitchToAutoSurveyMode(EventSwitchToAutoMap event)
    {
        Logger.log("SWITCH TO AUTOSURVEY MODE...");
        this.state_manualMoving.Leave();
        this.state_autoSurveying.Enter();
    }

    @Subscribe
    private void OnSTOP(EventEmergencySTOP event) {
        //Switch to idle mode
        Logger.log("EMERGENCY STOP...");
        this.state_autoSurveying.Leave();
        this.state_manualMoving.Leave();
        this.state_idle.Enter();
        //Sent event stop
    }
}
