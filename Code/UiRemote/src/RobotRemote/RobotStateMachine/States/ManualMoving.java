package RobotRemote.RobotStateMachine.States;

import RobotRemote.RobotServices.Movement.IMovementService;
import RobotRemote.RobotServices.Sensors.SensorsState;
import RobotRemote.RobotStateMachine.Events.ManualState.EventManualCommand;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Shared.Logger;
import RobotRemote.Shared.ServiceManager;
import RobotRemote.Shared.ThreadLoop;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.scene.paint.Color;

public class ManualMoving implements IModeState {
  private EventBus eventBus;
  private ServiceManager sm;
  private SensorsState sensorState;
  private ThreadLoop threadLoop;
  private IMovementService moveThread;
  private boolean IsOnState;
  public ManualMoving(ServiceManager sm) {
    this.IsOnState = false;
    this.sm = sm;
    this.eventBus = sm.getEventBus();
    this.sensorState = sm.getAppState().getSensorsState();
    this.threadLoop = sm.getRobotStateMachineThread();
    this.moveThread = sm.getMovementService();
  }

  public void Enter() {
    if(this.IsOnState)
        return;
    this.IsOnState =true;
    this.eventBus.register(this);
    this.threadLoop.StartThread(() -> {
      LoopThis();
      return null;
    }, 100);
    Logger.debug("ENTER MANUAL STATE...");
  }

  public void Leave() {
    if(!this.IsOnState)
      return;
    this.moveThread.stop();
    this.threadLoop.StopThread();
    Logger.debug("LEAVE MANUAL STATE...");
  }

  private void LoopThis() throws InterruptedException {
    double ultraDist = sensorState.getUltraReadingCm();
    Color colourEnum = sensorState.getColourEnum();
    if(ultraDist < 10) {
      //Logger.debug("Close to Object: " + ultraDist + " cm");
//      this.eventBus.post(new EventWarnOfObject(ultraDist));
    }
    if(colourEnum == Color.RED) {
      //Logger.debug("Crater Detected, ColourId: " + colourEnum);
//      this.eventBus.post(new EventWarnOfColour(colourEnum));
    }
  }

  @Subscribe
  public void OnManualCommand(EventManualCommand event) {
    try {
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
    } catch (InterruptedException e) {
      Logger.debug("MANUAL Command Interrupted, stopping");
      moveThread.stop();
    }
  }
}
