package RobotRemote.RobotStateMachine.States;

import RobotRemote.RobotServices.Movement.IMovementService;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Shared.Logger;
import RobotRemote.Shared.ServiceManager;
import com.google.common.eventbus.EventBus;

public class IdleState implements IModeState {
    private EventBus eventBus;
    private  boolean IsOnState;
    private IMovementService moveThread;
    public IdleState(ServiceManager sm) {
        this.IsOnState = false;
        this.eventBus = sm.getEventBus();
        this.moveThread = sm.getMovementService();
    }

    public void Enter() {
        if(this.IsOnState)
            return;
        this.moveThread.stop();
        this.IsOnState =true;
        this.eventBus.register(this);
        Logger.debug("ENTER IDLE STATE...");
    }

    public void Leave() {
        if(!this.IsOnState)
            return;
        this.IsOnState =false;
        this.eventBus.unregister(this);
        Logger.debug("LEAVE IDLE STATE...");
    }
}
