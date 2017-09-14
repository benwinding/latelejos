package RobotRemote.RobotStateMachine.States;

import RobotRemote.RobotServices.Movement.IMovementService;
import RobotRemote.RobotServices.Movement.MovementService;
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
  private ServiceManager sm;
  private EventBus eventBus;
  private SensorsState sensorState;
  private ThreadLoop threadLoop;

  private ManualStopped state_manualstopped;
  private AutoObjectAvoiding state_autoObjectAvoiding;

  public AutoSurveying(ServiceManager sm) {
    this.sm = sm;
    this.eventBus = sm.getEventBus();
    this.sensorState = sm.getAppState().getSensorsState();
    this.threadLoop = sm.getRobotStateMachineThread();
  }

  public void linkStates(ManualStopped state_manualstopped, AutoObjectAvoiding state_autoObjectAvoiding) {
    this.state_manualstopped = state_manualstopped;
    this.state_autoObjectAvoiding = state_autoObjectAvoiding;
  }

  @Override
  public void EnterState() {
    this.eventBus.register(this);
    this.threadLoop.StartThread(this::LoopThis, 100);
  }

  private void LoopThis() {
    IMovementService moveThread = sm.getMovementService();
    moveThread.forward(10);
    moveThread.doWhileMoving(new MoveCallback() {
      @Override
      public void movingLoop() {
        if (isThereATrail()) {
          HandleDetectedTrail();
        }
      }

      @Override
      public void onCancel() {
        threadLoop.StopThread();
      }
    });
    moveThread.turn(90);
    moveThread.doWhileMoving(new MoveCallback() {
      @Override
      public void movingLoop() {
        if (isThereATrail()) {
          HandleDetectedTrail();
        }
      }

      @Override
      public void onCancel() {
        threadLoop.StopThread();
      }
    });
    moveThread.forward(10);
    moveThread.doWhileMoving(new MoveCallback() {
      @Override
      public void movingLoop() {
        if (isThereATrail()) {
          HandleDetectedTrail();
        }
      }

      @Override
      public void onCancel() {
        threadLoop.StopThread();
      }
    });
    moveThread.turn(90);
    moveThread.doWhileMoving(new MoveCallback() {
      @Override
      public void movingLoop() {
        if (isThereATrail()) {
          HandleDetectedTrail();
        }
      }

      @Override
      public void onCancel() {
        threadLoop.StopThread();
      }
    });
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
    this.sm.getMovementService().stop();
    this.threadLoop.StopThread();
    state_manualstopped.EnterState();
  }
}
