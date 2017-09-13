package RobotRemote.RobotStateMachine.States;

import RobotRemote.RobotServices.Movement.IMovementService;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Shared.ServiceManager;
import RobotRemote.Shared.ThreadLoop;
import com.google.common.eventbus.EventBus;

public class AutoSurveyZigZag implements IModeState{
  private EventBus eventBus;
  private ThreadLoop loopThread;
  private IMovementService moveThread;
  private ManualStopped state_manualstopped;

  public AutoSurveyZigZag(ServiceManager sm) {
    this.eventBus = sm.getEventBus();
    this.loopThread = sm.getRobotStateMachineThread();
    this.moveThread = sm.getMovementService();
  }

  @Override
  public void EnterState() {
    this.eventBus.register(this);
    this.loopThread.StopThread();
    this.loopThread.StartThread(() -> {
      moveThread.Forward(10);
      Sleep(3000);
      moveThread.Turn(90);
      Sleep(3000);
      moveThread.Forward(10);
      Sleep(3000);
      moveThread.Turn(-90);
      Sleep(3000);
    }, 100);
  }

  private void Sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException ignored) {
    }
  }

  public void linkStates(ManualStopped state_manualstopped) {
    this.state_manualstopped = state_manualstopped;
  }
}
