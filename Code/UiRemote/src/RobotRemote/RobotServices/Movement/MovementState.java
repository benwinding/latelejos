package RobotRemote.RobotServices.Movement;

import RobotRemote.Models.Enums.EnumMotors;

import static RobotRemote.Models.Enums.EnumMotors.Stationary;

public class MovementState {
  private double motorsSpeedLinear;
  private EnumMotors motorState = Stationary;

  public double getMotorsSpeedLinear() {
    return motorsSpeedLinear;
  }

  void setMotorsSpeedLinear(double motorsSpeedLinear) {
    this.motorsSpeedLinear = motorsSpeedLinear;
  }

  public EnumMotors getMotorState() {
    return motorState;
  }

  public synchronized void setMotorState(EnumMotors motorState) {
    this.motorState = motorState;
  }
}
