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
    this.isMoving = true;
    try{
      Thread.sleep(500);
    }catch (Exception ignored) {

    }
  }

  @Override
  public void rotate(double v, boolean b) {
    this.isMoving = true;
  }

  @Override
  public void setAngularSpeed(double v) {
    this.angularSpeed = v;
  }

  @Override
  public double getAngularSpeed() {
    return this.angularSpeed*ratio;
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

    this.isMoving = true;
  }

  @Override
  public void rotateLeft() {
    this.isMoving = true;
  }

  @Override
  public void forward() {
    this.isMoving = true;
  }

  @Override
  public void backward() {
    this.isMoving = true;
  }

  @Override
  public void stop() {
    this.isMoving = false;
  }

  boolean isMoving = false;

  @Override
  public boolean isMoving() {
    return isMoving;
  }

  @Override
  public void travel(double v) {

  }

  @Override
  public void travel(double v, boolean b) {

  }
  private int ratio =2;
  @Override
  public void setLinearSpeed(double v) {
    this.linearSpeed = v;
  }

  @Override
  public double getLinearSpeed() {
    return this.linearSpeed*ratio;
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
