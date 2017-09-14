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
  private SensorsState sensorState;
  private EventBus eventBus;
  private ThreadLoop loopThread;
  private IMovementService moveThread;
  private ManualStopped state_manualstopped;
  private AutoObjectAvoiding state_autoObjectAvoiding;

  public AutoSurveying(ServiceManager sm) {
    this.eventBus = sm.getEventBus();
    this.loopThread = sm.getRobotStateMachineThread();
    this.moveThread = sm.getMovementService();
    this.sensorState = sm.getAppState().getSensorsState();
  }

  public void linkStates(ManualStopped state_manualstopped, AutoObjectAvoiding state_autoObjectAvoiding) {
    this.state_manualstopped = state_manualstopped;
    this.state_autoObjectAvoiding = state_autoObjectAvoiding;
  }

  @Override
  public void EnterState() {
    this.eventBus.register(this);
    this.loopThread.StopThread();
    this.loopThread.StartThread(this::LoopThis, 100);
  }

  private void LoopThis() {
    moveThread.forward(10);
    moveThread.doWhileMoving(() -> {
      if (isThereABorder()) {
        HandleDetectedBorder();
      }
      else if (isThereATrail()) {
        HandleDetectedTrail();
      }
      else if (isThereACrater()) {
        HandleDetectedCrater();
      }
      else if (isThereAnObject()) {
        HandleDetectedObject();
      }
    });
    moveThread.turn(90);
    moveThread.doWhileMoving(() -> {
      if (isThereABorder()) {
        HandleDetectedBorder();
      }
      else if (isThereATrail()) {
        HandleDetectedTrail();
      }
      else if (isThereACrater()) {
        HandleDetectedCrater();
      }
      else if (isThereAnObject()) {
        HandleDetectedObject();
      }
    });
//    while(moveThread.isMoving()) {
//      if (isThereABorder()) {
//        HandleDetectedBorder();
//      }
//      else if (isThereATrail()) {
//        HandleDetectedTrail();
//      }
//      else if (isThereACrater()) {
//        HandleDetectedCrater();
//      }
//      else if (isThereAnObject()) {
//        HandleDetectedObject();
//      }
//      Sleep(50);
//    }
//    DoUTurnLeft();
//    moveThread.forward(10);
//    while(moveThread.isMoving()) {
//      if (isThereABorder()) {
//        HandleDetectedBorder();
//      }
//      else if (isThereATrail()) {
//        HandleDetectedTrail();
//      }
//      else if (isThereACrater()) {
//        HandleDetectedCrater();
//      }
//      else if (isThereAnObject()) {
//        HandleDetectedObject();
//      }
//      Sleep(50);
//    }
//    DoUTurnRight();
  }

  private void DoUTurnLeft() {
    moveThread.turn(-90);
    while(moveThread.isMoving()) {
      // Detect sensors here?
      Sleep(50);
    }
    moveThread.forward(10);
    while(moveThread.isMoving()) {
      // Detect sensors here?
      Sleep(50);
    }
    moveThread.turn(-90);
    while(moveThread.isMoving()) {
      // Detect sensors here?
      Sleep(50);
    }
  }

  private void DoUTurnRight() {
    moveThread.turn(90);
    while(moveThread.isMoving()) {
      // Detect sensors here?
      Sleep(50);
    }
    moveThread.forward(10);
    while(moveThread.isMoving()) {
      // Detect sensors here?
      Sleep(50);
    }
    moveThread.turn(90);
    while(moveThread.isMoving()) {
      // Detect sensors here?
      Sleep(50);
    }
  }

  private void HandleDetectedObject() {
    Logger.log("Handling Detected Object");
  }

  private void HandleDetectedCrater() {
    Logger.log("Handling Detected Crater");
  }

  private void HandleDetectedTrail() {
    Logger.log("Handling Detected Trail");
  }

  private void HandleDetectedBorder() {
    Logger.log("Handling Detected Border");
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
    this.eventBus.unregister(this);
    this.moveThread.stop();
    this.loopThread.StopThread();
    state_manualstopped.EnterState();
  }

  private void Sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException ignored) {
      this.moveThread.stop();
      this.loopThread.StopThread();
    }
  }
}
