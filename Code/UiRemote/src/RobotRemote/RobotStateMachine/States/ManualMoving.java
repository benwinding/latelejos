package RobotRemote.RobotStateMachine.States;

import RobotRemote.Shared.*;
import RobotRemote.RobotStateMachine.Events.*;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.RobotServices.Movement.IMovementService;
import RobotRemote.RobotServices.Sensors.SensorsState;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.scene.paint.Color;

public class ManualMoving implements IModeState {
  private EventBus eventBus;
  private ServiceManager sm;
  private SensorsState sensorState;

  private ThreadLoop threadLoop;

  private ManualStopped state_manualstopped;

  public ManualMoving(ServiceManager sm) {
    this.sm = sm;
    this.eventBus = sm.getEventBus();
    this.sensorState = sm.getAppState().getSensorsState();
    this.threadLoop = sm.getRobotStateMachineThread();
  }

  public void linkStates(ManualStopped state_waiting) {
    this.state_manualstopped = state_waiting;
  }

  public void EnterState() {
    Logger.log("STATE: ManualMoving...");
    this.eventBus.register(this);
    threadLoop.StartThread(this::LoopThis,500);
  }

  private void LoopThis() {
    double ultraDist = sensorState.getUltraReadingCm();
    Color colourEnum = sensorState.getColourEnum();
    if(ultraDist < 10) {
      //Logger.log("Close to Object: " + ultraDist + " cm");
//      this.eventBus.post(new EventWarnOfObject(ultraDist));
    }
    if(colourEnum == Color.RED) {
      //Logger.log("Crater Detected, ColourId: " + colourEnum);
//      this.eventBus.post(new EventWarnOfColour(colourEnum));
    }
  }

  @Subscribe
  public void OnManualCommand(EventManualCommand event) {
    IMovementService moveThread = this.sm.getMovementService();
    switch (event.getCommand()) {
      case Left:
        moveThread.turn(-90);
        break;
      case Right:
        moveThread.turn(90);
        break;
      case Forward:
        moveThread.forward();
        break;
      case Backward:
        moveThread.backward();
        break;
      case Halt:
        moveThread.stop();
        break;
    }
  }

  @Subscribe
  private void OnExitManualControl(EventExitManualControl event) {
    this.eventBus.unregister(this);
    this.sm.getMovementService().stop();
    this.threadLoop.StopThread();
    state_manualstopped.EnterState();
  }

  @Subscribe
  private void OnSTOP(EventEmergencySTOP event) {
    this.eventBus.unregister(this);
    this.sm.getMovementService().stop();
    this.threadLoop.StopThread();
    state_manualstopped.EnterState();
  }
}
