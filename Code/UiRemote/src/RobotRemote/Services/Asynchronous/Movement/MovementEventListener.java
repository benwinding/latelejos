package RobotRemote.Services.Asynchronous.Movement;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.Events.EventManualControl;
import RobotRemote.Models.RobotConfig;
import RobotRemote.Repositories.RobotRepository;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lejos.robotics.navigation.ArcRotateMoveController;

public final class MovementEventListener {
  private final MoveStraightThread moveStraightThread;
  private final MoveTurnSynchronous moveTurnSynchronous;

  public MovementEventListener(RobotConfig config, ArcRotateMoveController pilot, RobotRepository robotRepository, EventBus eventBus) {
    this.moveStraightThread = new MoveStraightThread(config, pilot, robotRepository.getLocationState(), robotRepository.getMovementState());
    this.moveTurnSynchronous = new MoveTurnSynchronous(pilot, robotRepository.getLocationState(), robotRepository.getMovementState());
    eventBus.register(this);
  }

  @Subscribe
  public void OnManualControl(EventManualControl manualControl) {
    Logger.LogCrossThread("Received Manual Command: " + manualControl.getCommand());
    switch (manualControl.getCommand()) {
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
}

