package RobotRemote.RobotStateMachine;

import RobotRemote.RobotStateMachine.Events.Shared.EventEmergencySTOP;
import RobotRemote.RobotStateMachine.Events.Shared.EventSwitchToAutoMap;
import RobotRemote.RobotStateMachine.Events.Shared.EventSwitchToManual;
import RobotRemote.RobotStateMachine.States.AutoMode.AutoSurveying;
import RobotRemote.RobotStateMachine.States.IdleState;
import RobotRemote.RobotStateMachine.States.ManualMoving;
import RobotRemote.Shared.Logger;
import RobotRemote.Shared.ServiceManager;
import com.google.common.eventbus.Subscribe;

public class StateMachineBuilder {
    private IdleState stateIdle;
    private ManualMoving stateManualMoving;
    private AutoSurveying stateAutoSurveying;
    private IModeState currentState;
    public StateMachineBuilder(ServiceManager sm) {
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

    @Subscribe
    private void OnSwitchToManualMode(EventSwitchToManual event) {
        Logger.log("SWITCH TO MANUAL MODE...");
        this.stateIdle.Leave();
        this.stateAutoSurveying.Leave();
        this.stateManualMoving.Enter();
        this.currentState = stateManualMoving;
    }

    @Subscribe
    private void OnSwitchToAutoSurveyMode(EventSwitchToAutoMap event) {
        Logger.log("SWITCH TO AUTOSURVEY MODE...");
        this.stateIdle.Leave();
        this.stateManualMoving.Leave();
        this.stateAutoSurveying.Enter();
        this.currentState = stateAutoSurveying;
    }

    @Subscribe
    private void OnSTOP(EventEmergencySTOP event) {
      Logger.log("EMERGENCY STOP...");
      this.stateAutoSurveying.Leave();
      this.stateManualMoving.Leave();
      //Switch to idle mode
      this.stateIdle.Enter();
      this.currentState = stateIdle;
    }

    @Subscribe
    private void OnEveryEvent(Object event) {
        Logger.debug("EVENT:: " + event.getClass().getSimpleName());
    }
}
