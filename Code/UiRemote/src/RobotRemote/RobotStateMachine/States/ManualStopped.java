package RobotRemote.RobotStateMachine.States;

import RobotRemote.RobotServices.Movement.IMovementService;
import RobotRemote.Shared.Logger;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Shared.ServiceManager;
import com.google.common.eventbus.EventBus;

public class ManualStopped implements IModeState {
    private EventBus eventBus;
    private  boolean IsOnState;
    private IMovementService moveThread;
    public ManualStopped(ServiceManager sm) {
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
        Logger.log("ENTER IDLE STATE...");
    }

    public void Leave() {
        if(!this.IsOnState)
            return;
        this.IsOnState =false;
        this.eventBus.register(this);
        Logger.log("LEAVE IDLE STATE...");
    }

}
