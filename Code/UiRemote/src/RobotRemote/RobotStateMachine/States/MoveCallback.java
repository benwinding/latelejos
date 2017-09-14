package RobotRemote.RobotStateMachine.States;

public interface MoveCallback {
  void movingLoop();
  void onCancel();
}
