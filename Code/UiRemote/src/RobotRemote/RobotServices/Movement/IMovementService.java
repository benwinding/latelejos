package RobotRemote.RobotServices.Movement;

import RobotRemote.Models.RobotConfiguration;
import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.RobotStateMachine.States.MoveCallback;
import RobotRemote.Shared.AppStateRepository;

public interface IMovementService {
  void stop();
  void forward();
  void forward(float dist_cm);
  void backward();
  void backward(float dist_cm);
  void turn(float degrees);
  boolean isMoving();
  void Initialize(RobotConfiguration configuration, RobotConnectionService robotConnectionService, AppStateRepository appStateRepository);

  void doWhileMoving(MoveCallback moveCallback);
}
