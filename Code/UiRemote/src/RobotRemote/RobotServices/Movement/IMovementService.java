package RobotRemote.RobotServices.Movement;

import RobotRemote.Models.RobotConfiguration;
import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.Shared.AppStateRepository;

public interface IMovementService {
  void Stop();
  void Forward();
  void Forward(float dist_cm);
  void Backward();
  void Backward(float dist_cm);
  void Turn(float degrees);
  boolean IsMoving();
  void Initialize(RobotConfiguration configuration, RobotConnectionService robotConnectionService, AppStateRepository appStateRepository);
}
