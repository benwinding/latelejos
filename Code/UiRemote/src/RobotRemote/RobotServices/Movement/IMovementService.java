package RobotRemote.RobotServices.Movement;

import RobotRemote.Shared.RobotConfiguration;
import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.Shared.AppStateRepository;

public interface IMovementService {
  void stop();
  void forward();
  void forward(float dist_cm);
  void backward();
  void backward(float dist_cm);
  void turn(int degrees);
  boolean isMoving();
  void Initialize(RobotConfiguration configuration, RobotConnectionService robotConnectionService, AppStateRepository appStateRepository);

  void repeatWhileMoving(Runnable repeatThis) throws InterruptedException;
  void waitWhileMoving() throws InterruptedException;
}
