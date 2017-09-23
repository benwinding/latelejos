package RobotRemote.RobotStateMachine.States;

import RobotRemote.RobotStateMachine.Events.ManualState.EventManualCommand;
import RobotRemote.Shared.*;
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
  private boolean IsOnState;
  public ManualMoving(ServiceManager sm) {
    this.IsOnState = false;
    this.sm = sm;
    this.eventBus = sm.getEventBus();
    this.sensorState = sm.getAppState().getSensorsState();
    this.threadLoop = sm.getRobotStateMachineThread();
  }

  public void Enter()
  {
        if(this.IsOnState)
            return;
        this.IsOnState =true;
        this.eventBus.register(this);
        threadLoop.StartThread(this::LoopThis,500);
        Logger.log("ENTER MANUAL STATE...");
  }

  public void Leave()
  {
      if(!this.IsOnState)
          return;
      this.IsOnState = false;
      this.eventBus.unregister(this);
      this.sm.getMovementService().stop();
      this.threadLoop.StopThread();
      Logger.log("LEAVE MANUAL STATE...");
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

}
