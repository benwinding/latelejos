package RobotRemote.Services.Asynchronous.Movement;

import RobotRemote.Models.MotorsEnum;
import lejos.robotics.navigation.ArcRotateMoveController;

class MoveTurnSynchronous {
  private ArcRotateMoveController pilot;
  private LocationState locationState;
  private MovementState movementState;

  MoveTurnSynchronous(ArcRotateMoveController pilot, LocationState locationState, MovementState movementState) {
    this.pilot = pilot;
    this.locationState = locationState;
    this.movementState = movementState;
  }

  public void TurnLeft() {
    this.movementState.setMotorState(MotorsEnum.TurningLeft);
    this.UpdateTurn(-90);
    this.movementState.setMotorState(MotorsEnum.Stationary);
  }

  public void TurnRight() {
    this.movementState.setMotorState(MotorsEnum.TurningRight);
    this.UpdateTurn(90);
    this.movementState.setMotorState(MotorsEnum.Stationary);
  }

  private void UpdateTurn(int angle) {
    this.pilot.rotate(angle);
    this.locationState.ChangingHeading(angle);
  }
}
