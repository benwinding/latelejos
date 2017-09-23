package RobotRemote.RobotStateMachine.States;

import RobotRemote.RobotServices.Movement.IMovementService;
import RobotRemote.RobotServices.Sensors.SensorsState;
import RobotRemote.RobotStateMachine.Events.EventEmergencySTOP;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Shared.Logger;
import RobotRemote.Shared.ServiceManager;
import RobotRemote.Shared.ThreadLoop;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.scene.paint.Color;

public class AutoSurveying implements IModeState{
  private IMovementService moveThread;
  private ServiceManager sm;
  private EventBus eventBus;
  private SensorsState sensorState;
  private ThreadLoop threadLoop;

  private ManualStopped state_manualstopped;

  public AutoSurveying(ServiceManager sm) {
    this.sm = sm;
    this.eventBus = sm.getEventBus();
    this.sensorState = sm.getAppState().getSensorsState();
    this.threadLoop = sm.getRobotStateMachineThread();
    this.moveThread = sm.getMovementService();
  }

  public void linkStates(ManualStopped state_manualstopped) {
    this.state_manualstopped = state_manualstopped;
  }

  @Override
  public void EnterState() {
    this.eventBus.register(this);
    this.threadLoop.StartThread(this::LoopThis, 100);
  }

  private void LoopThis() {
    ZigZagAcrossMap();
  }

  private void ZigZagAcrossMap() {
    try {
      // Move leftwards across the map
      moveThread.forward();
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
      moveThread.forward();
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
    } catch (InterruptedException e) {
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

  @Subscribe
  private void OnSTOP(EventEmergencySTOP event) {
    Logger.log("Autosurveying:  EventEmergencyStop");
    this.eventBus.unregister(this);
    this.moveThread.stop();
    this.threadLoop.StopThread();
    state_manualstopped.EnterState();
  }
}
