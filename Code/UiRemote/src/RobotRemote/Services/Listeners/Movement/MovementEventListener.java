package RobotRemote.Services.Listeners.Movement;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.Events.EventManualControl;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Repositories.AppStateRepository;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lejos.robotics.navigation.ArcRotateMoveController;

public final class MovementEventListener {
  private final MoveStraightThread moveStraightThread;
  private final MoveTurnSynchronous moveTurnSynchronous;

  public MovementEventListener(RobotConfiguration config, ArcRotateMoveController pilot, AppStateRepository appStateRepository, EventBus eventBus) {
    this.moveStraightThread = new MoveStraightThread(config, pilot, appStateRepository.getLocationState(), appStateRepository.getMovementState());
    this.moveTurnSynchronous = new MoveTurnSynchronous(pilot, appStateRepository.getLocationState(), appStateRepository.getMovementState());
    eventBus.register(this);
  }

  @Subscribe
  public void OnManualControl(EventManualControl event) {
    Logger.LogCrossThread("Received Manual Command: " + event.getCommand());
    switch (event.getCommand()) {
      case Forward:
        this.moveStraightThread.MoveForward();
        break;
      case Backward:
        this.moveStraightThread.MoveBackward();
        break;
      case Left:
        this.moveStraightThread.kill();
        this.moveTurnSynchronous.TurnLeft();
        break;
      case Right:
        this.moveStraightThread.kill();
        this.moveTurnSynchronous.TurnRight();
        break;
      case Stop:
      case Ignore:
      default:
        this.moveStraightThread.kill();
    }
  }

  public void closeMotors() {
    this.moveStraightThread.closeMotors();
  }
}

