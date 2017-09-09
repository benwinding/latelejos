package RobotRemote.Services.Movement.MoveThreads;

import RobotRemote.Helpers.Synchronizer;
import RobotRemote.Models.MotorsEnum;
import lejos.robotics.navigation.ArcRotateMoveController;

public class MoveTurnSynchronous {
  private ArcRotateMoveController pilot;
  private LocationState locationState;
  private MovementState movementState;

  public MoveTurnSynchronous(ArcRotateMoveController pilot, LocationState locationState, MovementState movementState) {
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
    Synchronizer.RunNotConcurrent(() -> {
        double adjustAngle = angle<0? angle*0.88: angle*0.93;
      this.pilot.rotate((int)adjustAngle );
      this.locationState.ChangingHeading(angle);
    });
  }
}
