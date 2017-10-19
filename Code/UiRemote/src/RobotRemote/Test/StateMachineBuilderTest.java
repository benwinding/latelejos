package RobotRemote.Test;

import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.RobotServices.Movement.IMovementService;
import RobotRemote.RobotServices.Movement.MovementService;
import RobotRemote.RobotServices.Sensors.SensorsService;
import RobotRemote.RobotStateMachine.Events.Shared.EventEmergencySTOP;
import RobotRemote.RobotStateMachine.Events.Shared.EventSwitchToAutoMap;
import RobotRemote.RobotStateMachine.Events.Shared.EventSwitchToManual;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.RobotStateMachine.StateMachineBuilder;
import RobotRemote.RobotStateMachine.States.AutoMode.AutoSurveying;
import RobotRemote.RobotStateMachine.States.IdleState;
import RobotRemote.RobotStateMachine.States.ManualMoving;
import RobotRemote.Shared.*;
import RobotRemote.UIServices.UiUpdater.UiUpdaterService;
import com.google.common.eventbus.EventBus;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StateMachineBuilderTest
{
  private ServiceManager getServiceManager(){

    // Get Application Configuration
    RobotConfiguration robotConfiguration = new RobotConfiguration();

    // Instantiate all app state
    AppStateRepository appStateRepository = new AppStateRepository(robotConfiguration);

    // Connection to the robot
    RobotConnectionService robotConnectionService = new RobotConnectionService(appStateRepository);

    // Instantiate EventBus
    EventBus eventBus = new EventBus();

    // Daemons
    SensorsService sensorService = new SensorsService(robotConfiguration, robotConnectionService, appStateRepository, eventBus);
    UiUpdaterService uiUpdaterService = new UiUpdaterService(robotConfiguration, appStateRepository, null);

    IMovementService movementService = new MovementService();
    ThreadLoop stateMachineThreadLoop = new ThreadLoop("Robot State Machine");
    Logger.isDisableLog =true;
    return  new ServiceManager(
        eventBus,
        robotConfiguration,
        appStateRepository,
        robotConnectionService,
        sensorService,
        uiUpdaterService,
        movementService,
        stateMachineThreadLoop
    );

  }

  @Test
  public void autoSurveyModeEnterTest(){
      ServiceManager  serviceManager = getServiceManager();
     // serviceManager.startAllThreads();
      StateMachineBuilder sm = new StateMachineBuilder(serviceManager);
      serviceManager.getEventBus().post(new EventSwitchToAutoMap());
      IModeState currState = sm.getCurrentState();
      Assert.assertTrue(currState instanceof AutoSurveying);

  }

  @Test
  public void manualModeEnterTest(){
    ServiceManager  serviceManager = getServiceManager();
    // serviceManager.startAllThreads();
    StateMachineBuilder sm = new StateMachineBuilder(serviceManager);
    serviceManager.getEventBus().post(new EventSwitchToManual());
    IModeState currState = sm.getCurrentState();
    Assert.assertTrue(currState instanceof ManualMoving);

  }


  @Test
  public void idleModeEnterTest(){
    ServiceManager  serviceManager = getServiceManager();
    // serviceManager.startAllThreads();
    StateMachineBuilder sm = new StateMachineBuilder(serviceManager);
    IModeState currState = sm.getCurrentState();
    Assert.assertTrue(currState instanceof IdleState);
    serviceManager.getEventBus().post(new EventSwitchToManual());
     currState = sm.getCurrentState();
    Assert.assertTrue(currState instanceof ManualMoving);
    serviceManager.getEventBus().post(new EventEmergencySTOP());
    currState = sm.getCurrentState();
    Assert.assertTrue(currState instanceof IdleState);

  }
}
