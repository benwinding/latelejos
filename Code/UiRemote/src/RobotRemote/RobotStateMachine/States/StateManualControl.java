package RobotRemote.RobotStateMachine.States;

import RobotRemote.Helpers.Logger;
import RobotRemote.RobotStateMachine.Events.EventExitManualControl;
import RobotRemote.RobotStateMachine.Events.EventManualCommand;
import RobotRemote.RobotStateMachine.Events.EventManualDetectedObject;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Services.Movement.MoveThreads.IMoveThread;
import RobotRemote.Services.ServiceLocator;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class StateManualControl implements IModeState {
  private EventBus eventBus;
  private ServiceLocator serviceLocator;

  private StateWaiting state_waiting;
  private StateManualWarn state_warn;

  public StateManualControl(EventBus eventBus, ServiceLocator serviceLocator) {
    this.eventBus = eventBus;
    this.serviceLocator = serviceLocator;
  }

  public void linkStates(StateWaiting state_waiting, StateManualWarn state_warn) {
    this.state_waiting = state_waiting;
    this.state_warn = state_warn;
  }

  public void EnterState() {
    Logger.log("STATE: StateManualControl...");
    this.eventBus.register(this);
  }

  @Subscribe
  public void OnManualCommand(EventManualCommand event) {
    IMoveThread moveThread = this.serviceLocator.getMoveThread();
    switch (event.getCommand()) {
      case Left:
        moveThread.Turn(-90);
        break;
      case Right:
        moveThread.Turn(90);
        break;
      case Forward:
        moveThread.Forward();
        break;
      case Backward:
        moveThread.Backward();
        break;
      case Halt:
        moveThread.Stop();
        break;
    }
  }

  @Subscribe
  private void OnExitManualControl(EventExitManualControl event) {
    this.eventBus.unregister(this);
    this.serviceLocator.getMoveThread().Stop();
    state_waiting.EnterState();
  }

  @Subscribe
  private void OnDetectObject(EventManualDetectedObject event) {
    this.eventBus.unregister(this);
    state_warn.EnterState();
  }
}
