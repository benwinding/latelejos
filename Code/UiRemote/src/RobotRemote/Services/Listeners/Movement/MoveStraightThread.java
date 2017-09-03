package RobotRemote.Services.Listeners.Movement;

import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Services.RobotWorkerBase;
import lejos.robotics.navigation.ArcRotateMoveController;

import static RobotRemote.Models.MotorsEnum.*;

class MoveStraightThread extends RobotWorkerBase {
  private double linearDistanceInterval;
  private MovementState movementState;
  private ArcRotateMoveController pilot;
  private LocationState locationState;
  private boolean movingForwards;

  MoveStraightThread(RobotConfiguration config, ArcRotateMoveController pilot, LocationState locationState, MovementState movementState) {
    super("Move Straight", config.updateIntervalMoving_ms);
    this.pilot = pilot;
    this.locationState = locationState;
    this.linearDistanceInterval = config.robotLinearSpeed_cms * (config.updateIntervalMoving_ms*1.0 / 1000);
    this.movementState = movementState;
    this.pilot.stop(); // If already moving stop
  }

  public void MoveForward() {
    this.movingForwards = true;
    this.pilot.forward();
    this.movementState.setMotorState(MovingForward);
    this.start();
  }

  public void MoveBackward() {
    this.movingForwards = false;
    this.pilot.backward();
    this.movementState.setMotorState(MovingBackward);
    this.start();
  }

  @Override
  protected void Repeat() {
    if(this.movingForwards)
      this.locationState.GoingStraight(-this.linearDistanceInterval);
    else
      this.locationState.GoingStraight(this.linearDistanceInterval);
  }

  @Override
  protected void OnShutdown() {
    if(this.pilot.isMoving())
      this.pilot.stop();
    this.movementState.setMotorState(Stationary);
  }

  void shutdownMotors() {
    this.pilot.stop();
    this.kill();
  }
}
