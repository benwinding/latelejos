package RobotRemote.Services.Movement;

import RobotRemote.Models.MotorsEnum;

import static RobotRemote.Models.MotorsEnum.Stationary;

public class MovementState {
  private double motorsSpeedLinear;
  private MotorsEnum motorState = Stationary;

  public double getMotorsSpeedLinear() {
    return motorsSpeedLinear;
  }

  void setMotorsSpeedLinear(double motorsSpeedLinear) {
    this.motorsSpeedLinear = motorsSpeedLinear;
  }

  public MotorsEnum getMotorState() {
    return motorState;
  }

  void setMotorState(MotorsEnum motorState) {
    this.motorState = motorState;
  }
}
