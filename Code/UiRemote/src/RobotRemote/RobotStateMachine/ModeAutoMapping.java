package RobotRemote.RobotStateMachine;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.Enums.EnumCommandManual;
import RobotRemote.Models.Events.EventAutonomousControl;
import RobotRemote.Models.Events.EventManualControl;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.Services.RobotServiceBase;
import com.google.common.collect.EvictingQueue;
import com.google.common.eventbus.EventBus;

public class ModeAutoMapping extends RobotServiceBase {
    enum AutonomousState{
        Moving,
        Stop,
        ObstacleDetected,
        AvoidObstacle
    }
    private AutonomousState currentState;
    private EventBus eventBus;
    private EvictingQueue<Double> obstacleDistanceValue;
    private EvictingQueue<EventManualControl> movementQueue;
    private AppStateRepository appStateRepository;
    public ModeAutoMapping(EventBus eventBus,AppStateRepository appStateRepository) {
        super("Mode Automatic", 100);
        this.eventBus = eventBus;
        this.appStateRepository = appStateRepository;
        movementQueue = EvictingQueue.create(100);
        obstacleDistanceValue = EvictingQueue.create(10);
    }

    private boolean DetectObstacle()
    {
        double distanceValue =this.appStateRepository.getSensorsState().getUltraReading() ;
            if(!Double.isInfinite(distanceValue) && distanceValue*100 < 6)
                return  true;
        return  false;
    }
    @Override
    protected void OnStart() {
        eventBus.post(new EventManualControl(EnumCommandManual.Stop));
        currentState =AutonomousState.Stop;
        Logger.log("Mode: Auto Mapping Started ...");
    }

    @Override
    protected void Repeat() {
        switch (currentState)
        {
            case Stop:
                eventBus.post(new EventManualControl(EnumCommandManual.Forward));
                currentState = AutonomousState.Moving;
                break;
            case Moving:
                if(DetectObstacle())
                {
                    eventBus.post(new EventManualControl(EnumCommandManual.Stop));
                    currentState = AutonomousState.ObstacleDetected;
                }
                break;
            case ObstacleDetected:
                //Some algorithm to move around obstacle
                //Create a set of command to move aground obstacle
                //currentState = AutonomousState.AvoidObstacle;
                eventBus.post(new EventAutonomousControl(EnumCommandManual.Left,0));
                eventBus.post(new EventAutonomousControl(EnumCommandManual.Forward,15));
                eventBus.post(new EventAutonomousControl(EnumCommandManual.Right,0));
                eventBus.post(new EventAutonomousControl(EnumCommandManual.Forward,40));
                eventBus.post(new EventAutonomousControl(EnumCommandManual.Right,0));
                eventBus.post(new EventAutonomousControl(EnumCommandManual.Forward,15));
                eventBus.post(new EventAutonomousControl(EnumCommandManual.Left,0));
                currentState = AutonomousState.Stop;
                break;
        }

    }

    @Override
    protected void OnShutdown() {
        currentState = AutonomousState.Stop;
        eventBus.post(new EventManualControl(EnumCommandManual.Stop));
        Logger.log("Mode: Auto Mapping ending ...");
    }
}
