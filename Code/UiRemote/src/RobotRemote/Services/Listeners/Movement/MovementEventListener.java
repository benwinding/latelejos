package RobotRemote.Services.Listeners.Movement;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.Events.EventAutoControl;
import RobotRemote.Models.Events.EventManualControl;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Repositories.AppStateRepository;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Waypoint;

public final class MovementEventListener {
  private final MoveStraightThread moveStraightThread;
  private final MoveTurnSynchronous moveTurnSynchronous;
  private final MovePrecise movePrecise;

  public MovementEventListener(RobotConfiguration config, ArcRotateMoveController pilot, AppStateRepository appStateRepository, EventBus eventBus) {
    this.moveStraightThread = new MoveStraightThread(config, pilot, appStateRepository.getLocationState(), appStateRepository.getMovementState());
    this.moveTurnSynchronous = new MoveTurnSynchronous(pilot, appStateRepository.getLocationState(), appStateRepository.getMovementState());
    this.movePrecise = new MovePrecise(pilot, appStateRepository.getLocationState(), appStateRepository.getMovementState());
    eventBus.register(this);
  }

  @Subscribe
  public void OnManualControl(EventManualControl event) {
    Logger.LogCrossThread("Received Manual Command: " + event.getCommand());
    this.movePrecise.stop();
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
        this.movePrecise.stop();
    }
  }

  @Subscribe
  public void OnPreciseControl(EventAutoControl event) {
    Waypoint nextWayPoint = event.getNextWayPoint();
    Logger.LogCrossThread("Received Precise Point to go to:: x:"+nextWayPoint.getX() + ",y:" + nextWayPoint.getY());
    this.moveStraightThread.kill();
    this.movePrecise.stop();
    this.movePrecise.moveToWaypoint(nextWayPoint);
  }

  public void shutdownMotors() {
    this.moveStraightThread.shutdownMotors();
  }
}
