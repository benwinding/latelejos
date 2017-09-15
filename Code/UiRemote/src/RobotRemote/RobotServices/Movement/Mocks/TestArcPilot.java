package RobotRemote.RobotServices.Movement.Mocks;

import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;

public class TestArcPilot implements ArcRotateMoveController {
  private double linearSpeed;
  private double angularSpeed;

  @Override
  public double getMinRadius() {
    return 0;
  }

  @Override
  public void setMinRadius(double v) {

  }

  @Override
  public void arcForward(double v) {

  }

  @Override
  public void arcBackward(double v) {

  }

  @Override
  public void arc(double v, double v1) {

  }

  @Override
  public void arc(double v, double v1, boolean b) {

  }

  @Override
  public void travelArc(double v, double v1) {

  }

  @Override
  public void travelArc(double v, double v1, boolean b) {

  }

  @Override
  public void rotate(double v) {
    try{
      Thread.sleep(500);
    }catch (Exception ignored) {

    }
  }

  @Override
  public void rotate(double v, boolean b) {

  }

  @Override
  public void setAngularSpeed(double v) {
    this.angularSpeed = v;
  }

  @Override
  public double getAngularSpeed() {
    return this.angularSpeed;
  }

  @Override
  public double getMaxAngularSpeed() {
    return 0;
  }

  @Override
  public void setAngularAcceleration(double v) {

  }

  @Override
  public double getAngularAcceleration() {
    return 0;
  }

  @Override
  public void rotateRight() {

  }

  @Override
  public void rotateLeft() {

  }

  @Override
  public void forward() {

  }

  @Override
  public void backward() {

  }

  @Override
  public void stop() {

  }

  @Override
  public boolean isMoving() {
    return false;
  }

  @Override
  public void travel(double v) {

  }

  @Override
  public void travel(double v, boolean b) {

  }

  @Override
  public void setLinearSpeed(double v) {
    this.linearSpeed = v;
  }

  @Override
  public double getLinearSpeed() {
    return this.linearSpeed;
  }

  @Override
  public double getMaxLinearSpeed() {
    return 0;
  }

  @Override
  public void setLinearAcceleration(double v) {

  }

  @Override
  public double getLinearAcceleration() {
    return 0;
  }

  @Override
  public Move getMovement() {
    return null;
  }

  @Override
  public void addMoveListener(MoveListener moveListener) {

  }
}
