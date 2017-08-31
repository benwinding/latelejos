package RobotRemote.Services.Asynchronous.Movement;

import RobotRemote.Models.RobotConfig;
import RobotRemote.Services.RobotServiceBase;
import lejos.robotics.navigation.ArcRotateMoveController;

class MoveStraightThread extends RobotServiceBase {
  private double linearDistanceInterval;
  private ArcRotateMoveController pilot;
  private LocationState locationState;
  private boolean movingForwards;

  MoveStraightThread(RobotConfig config, ArcRotateMoveController pilot, LocationState locationState, MovementState movementState) {
    super("Move Straight", config.updateIntervalMoving_ms);
    this.pilot = pilot;
    this.locationState = locationState;
    this.linearDistanceInterval = config.robotLinearSpeed_cms * (config.updateIntervalMoving_ms*1.0 / 1000);
  }

  public void MoveForward() {
    this.movingForwards = true;
    this.pilot.forward();
    this.start();
  }

  public void MoveBackward() {
    this.movingForwards = false;
    this.pilot.backward();
    this.start();
  }

  @Override
  protected void Repeat() {
    if(this.movingForwards)
      this.locationState.GoingStraight(-this.linearDistanceInterval);
    else
      this.locationState.GoingStraight(this.linearDistanceInterval);
  }
}
