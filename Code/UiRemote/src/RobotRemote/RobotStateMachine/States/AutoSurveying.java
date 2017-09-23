package RobotRemote.RobotStateMachine.States;

import RobotRemote.RobotServices.Movement.IMovementService;
import RobotRemote.RobotServices.Sensors.SensorsState;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Shared.Logger;
import RobotRemote.Shared.ServiceManager;
import RobotRemote.Shared.ThreadLoop;
import com.google.common.eventbus.EventBus;
import javafx.scene.paint.Color;

public class AutoSurveying implements IModeState{
    private boolean IsOnState;
    private IMovementService moveThread;
    private ServiceManager sm;
    private EventBus eventBus;
    private SensorsState sensorState;
    private ThreadLoop threadLoop;

  public AutoSurveying(ServiceManager sm) {
    this.sm = sm;
    this.eventBus = sm.getEventBus();
    this.sensorState = sm.getAppState().getSensorsState();
    this.threadLoop = sm.getRobotStateMachineThread();
    this.moveThread = sm.getMovementService();
  }

  public void Enter() {
        if(this.IsOnState)
            return;
        this.eventBus.register(this);
        this.threadLoop.StartThread(this::LoopThis, 100);
        this.IsOnState = true;
        Logger.log("ENTER AUTO SURVEY MODE...");
  }

    public void Leave(){
        if(!this.IsOnState)
            return;
        this.eventBus.unregister(this);
        this.threadLoop.StopThread();
        this.IsOnState = false;
        this.moveThread.stop();
        Logger.log("LEAVE AUTO SURVEY MODE...");
    }

  private void LoopThis() {
      if(!this.IsOnState)
          return;
        ZigZagAcrossMap();
  }

  private void ZigZagAcrossMap() {
    try {
        Logger.log("ZigZagAcrossMap");
      // Move leftwards across the map
      moveThread.forward(5);
      moveThread.repeatWhileMoving(() -> {
        if (isThereABorder()) {
          Logger.log("Detected Border");
          HandleDetectedBorderLeft();
        }
        if (isThereAnObject()) {
          Logger.log("Detected Object");
          HandleDetectedObjectLeft();
        }
      });
      // Move rightwards across the map
      moveThread.forward(5);
      moveThread.repeatWhileMoving(() -> {
        if (isThereABorder()) {
          Logger.log("Detected Border");
          HandleDetectedBorderRight();
        }
        if (isThereAnObject()) {
          Logger.log("Detected Object");
          HandleDetectedBorderRight();
        }
      });
    } catch (Exception e) {
      Logger.log("- Interrupt: AutoSurveying");
      this.sm.getMovementService().stop();
      this.threadLoop.StopThread();
    }
  }

  private void HandleDetectedObjectRight() {
    Logger.log("Handling Detected Object");
  }

  private void HandleDetectedObjectLeft() {
    Logger.log("Handling Detected Object going left");
    try {
      moveThread.turn(-90);
      moveThread.waitWhileMoving();
      moveThread.forward(15);
      moveThread.waitWhileMoving();
      moveThread.turn(90);
      moveThread.repeatWhileMoving(()->{
        if (isThereABorder()) {
          Logger.log("Detected Border");
          HandleDetectedBorderLeft();
        }
        if (isThereAnObject()) {
          Logger.log("Detected Object");
          HandleDetectedBorderLeft();
        }
      });
    } catch (InterruptedException e) {
      Logger.log("- Interrupt: AutoSurveying, Handle detected border left");
      this.sm.getMovementService().stop();
      this.threadLoop.StopThread();
    }
  }

  private void HandleDetectedCrater() {
    Logger.log("Handling Detected Crater");
  }

  private void HandleDetectedTrail() {
    Logger.log("Handling Detected Trail");
  }

  private void HandleDetectedBorderLeft() {
    Logger.log("Handling Detected Border going left");
    try {
      TurnRobotAround(true);
    } catch (InterruptedException e) {
      Logger.log("- Interrupt: AutoSurveying, Handle detected border left");
      this.sm.getMovementService().stop();
      this.threadLoop.StopThread();
    }
  }

  private void HandleDetectedBorderRight() {
    Logger.log("Handling Detected Border going right");
    try {
      TurnRobotAround(false);
    } catch (InterruptedException e) {
      Logger.log("- Interrupt: AutoSurveying, Handle detected border right");
      this.sm.getMovementService().stop();
      this.threadLoop.StopThread();
    }
  }

  private void TurnRobotAround(boolean rightTurn) throws InterruptedException {
    int turnAngle = 90;
    if(rightTurn)
      turnAngle = -90;
    moveThread.backward(10);
    moveThread.waitWhileMoving();
    moveThread.turn(-turnAngle);
    moveThread.waitWhileMoving();
    moveThread.forward(15);
    moveThread.waitWhileMoving();
    moveThread.turn(-turnAngle);
    moveThread.waitWhileMoving();
  }

  private boolean isThereAnObject() {
    return sensorState.getUltraReadingCm() < 10;
  }

  private boolean isThereABorder() {
    return sensorState.getColourEnum() == Color.RED;
  }

  private boolean isThereACrater() {
    return sensorState.getColourEnum() == Color.GREEN;
  }

  private boolean isThereATrail() {
    return sensorState.getColourEnum() == Color.YELLOW;
  }

}
