package RobotRemote.RobotStateMachine;

import RobotRemote.RobotStateMachine.Events.Shared.EventEmergencySTOP;
import RobotRemote.RobotStateMachine.Events.Shared.EventSwitchToAutoMap;
import RobotRemote.RobotStateMachine.Events.Shared.EventSwitchToManual;
import RobotRemote.RobotStateMachine.States.AutoSurveying;
import RobotRemote.RobotStateMachine.States.IdleState;
import RobotRemote.RobotStateMachine.States.ManualMoving;
import RobotRemote.Shared.Logger;
import RobotRemote.Shared.ServiceManager;
import com.google.common.eventbus.Subscribe;

public class StateMachineBuilder {
    private IdleState state_idle;
    private ManualMoving state_manualMoving;
    private AutoSurveying state_autoSurveying;

    public StateMachineBuilder(ServiceManager sm) {
        sm.getEventBus().register(this);
        // Create State Instances
        state_manualMoving = new ManualMoving(sm);
        state_autoSurveying = new AutoSurveying(sm);
        state_idle = new IdleState(sm);
        state_idle.Enter();
    }

    @Subscribe
    private void OnSwitchToManualMode(EventSwitchToManual event) {
        Logger.log("SWITCH TO MANUAL MODE...");
        this.state_idle.Leave();
        this.state_autoSurveying.Leave();
        this.state_manualMoving.Enter();
    }

    @Subscribe
    private void OnSwitchToAutoSurveyMode(EventSwitchToAutoMap event) {
        Logger.log("SWITCH TO AUTOSURVEY MODE...");
        this.state_idle.Leave();
        this.state_manualMoving.Leave();
        this.state_autoSurveying.Enter();
    }

    @Subscribe
    private void OnSTOP(EventEmergencySTOP event) {
      Logger.log("EMERGENCY STOP...");
      this.state_autoSurveying.Leave();
      this.state_manualMoving.Leave();
      //Switch to idle mode
      this.state_idle.Enter();
    }

    @Subscribe
    private void OnEveryEvent(Object event) {
        Logger.debug("EVENT:: " + event.getClass().getSimpleName());
    }
}
