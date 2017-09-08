package RobotRemote.Services.Movement;

import RobotRemote.Helpers.Synchronizer;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Services.RobotServiceBase;
import lejos.robotics.navigation.ArcRotateMoveController;

import static RobotRemote.Models.MotorsEnum.*;

class MoveStraightThread extends RobotServiceBase {
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
    Synchronizer.RunNotConcurrent(() -> {
      this.pilot.stop(); // If already moving stop
    });
  }

  public void MoveForward() {
    this.movingForwards = true;
    Synchronizer.RunNotConcurrent(() -> {
      this.pilot.forward();
    });
    this.movementState.setMotorState(MovingForward);
    this.start();
  }

  public void MoveBackward() {
    this.movingForwards = false;
    Synchronizer.RunNotConcurrent(() -> {
      this.pilot.backward();
    });
    this.movementState.setMotorState(MovingBackward);
    this.start();
  }

  @Override
  protected void Repeat() {
    if(this.movingForwards)
      this.locationState.GoingStraight(this.linearDistanceInterval);
    else
      this.locationState.GoingStraight(-this.linearDistanceInterval);
  }

  @Override
  protected void OnShutdown() {
    Synchronizer.RunNotConcurrent(() -> {
      if(this.pilot.isMoving())
        this.pilot.stop();
    });
    this.movementState.setMotorState(Stationary);
  }

  void shutdownMotors() {
    Synchronizer.RunNotConcurrent(() -> {
      if(this.pilot.isMoving())
        this.pilot.stop();
    });
    this.kill();
  }

  void MoveDistance(int distance) {
    Synchronizer.RunNotConcurrent(() -> {
      pilot.travel(distance, true);
      this.locationState.GoingStraight(distance);
    });
  }
}
