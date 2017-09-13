package RobotRemote.RobotServices.Movement;

public interface IMoveThread {
  void Stop();
  void Forward();
  void Forward(float dist_cm);
  void Backward();
  void Backward(float dist_cm);
  void Turn(float degrees);
}
