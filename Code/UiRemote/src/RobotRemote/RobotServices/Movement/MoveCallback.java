package RobotRemote.RobotServices.Movement;

public interface MoveCallback {
  void movingLoop();
  void onInterrupted();
  void onFinished();
}
