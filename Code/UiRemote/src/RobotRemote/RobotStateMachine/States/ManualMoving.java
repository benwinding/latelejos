package RobotRemote.RobotStateMachine.States;

import RobotRemote.Helpers.ColourTranslator;
import RobotRemote.Helpers.Logger;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.RobotStateMachine.Events.*;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Helpers.ThreadLoop;
import RobotRemote.Services.Movement.MoveThreads.IMoveThread;
import RobotRemote.Services.Sensors.SensorsState;
import RobotRemote.Services.ServiceLocator;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class ManualMoving implements IModeState {
  private EventBus eventBus;
  private ServiceLocator serviceLocator;
  private SensorsState sensorState;

  private ThreadLoop threadLoop = new ThreadLoop();

  private ManualStopped state_manualstopped;

  public ManualMoving(EventBus eventBus, ServiceLocator serviceLocator, AppStateRepository appStateRepository) {
    this.eventBus = eventBus;
    this.serviceLocator = serviceLocator;
    this.sensorState = appStateRepository.getSensorsState();
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
    int colourId = sensorState.getColourId();
    if(ultraDist < 10) {
      Logger.log("Close to Object: " + ultraDist + " cm");
      this.eventBus.post(new EventWarnOfObject(ultraDist));
    }
    if(ColourTranslator.GetColourName(colourId) == "RED") {
      Logger.log("Crater Detected, ColourId: " + colourId);
      this.eventBus.post(new EventWarnOfColour(colourId));
    }
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
    this.threadLoop.StopThread();
    state_manualstopped.EnterState();
  }

  @Subscribe
  private void OnSTOP(EventEmergencySTOP event) {
    this.eventBus.unregister(this);
    this.serviceLocator.getMoveThread().Stop();
    this.threadLoop.StopThread();
    state_manualstopped.EnterState();
  }
}
