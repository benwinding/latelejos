package RobotRemote.Repositories;

import RobotRemote.Models.MotorsEnum;

class MotorsState {
  private double motorsSpeedLinear;
  private MotorsEnum motorState;

  public double getMotorsSpeedLinear() {
    return motorsSpeedLinear;
  }

  public void setMotorsSpeedLinear(double motorsSpeedLinear) {
    this.motorsSpeedLinear = motorsSpeedLinear;
  }

  public MotorsEnum getMotorState() {
    return motorState;
  }

  public void setMotorState(MotorsEnum motorState) {
    this.motorState = motorState;
  }
}
