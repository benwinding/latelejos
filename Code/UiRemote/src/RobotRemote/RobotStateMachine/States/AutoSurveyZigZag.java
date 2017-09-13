package RobotRemote.RobotStateMachine.States;

import RobotRemote.RobotServices.Movement.IMovementService;
import RobotRemote.RobotStateMachine.Events.EventEmergencySTOP;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Shared.ServiceManager;
import RobotRemote.Shared.ThreadLoop;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class AutoSurveyZigZag implements IModeState{
  private EventBus eventBus;
  private ThreadLoop loopThread;
  private IMovementService moveThread;
  private ManualStopped state_manualstopped;
  private ServiceManager sm;

  public AutoSurveyZigZag(ServiceManager sm) {
    this.sm = sm;
    this.eventBus = sm.getEventBus();
    this.loopThread = sm.getRobotStateMachineThread();
    this.moveThread = sm.getMovementService();
  }

  public void linkStates(ManualStopped state_manualstopped) {
    this.state_manualstopped = state_manualstopped;
  }

  @Override
  public void EnterState() {
    this.eventBus.register(this);
    this.loopThread.StopThread();
    this.loopThread.StartThread(() -> {
      moveThread.Forward(10);
      // Sleep(3000);
      // moveThread.Turn(90);
      // Sleep(3000);
      // moveThread.Forward(10);
      // Sleep(3000);
      // moveThread.Turn(-90);
      Sleep(3000);
    }, 100);
  }


  @Subscribe
  private void OnSTOP(EventEmergencySTOP event) {
    this.eventBus.unregister(this);
    this.loopThread.StopThread();
    this.moveThread.Stop();
    state_manualstopped.EnterState();
  }

  private void Sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException ignored) {
    }
  }
}
