package RobotRemote.RobotServices.Movement;

import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.Shared.AppStateRepository;
import RobotRemote.Shared.RobotConfiguration;

import java.util.concurrent.Callable;

public interface IMovementService {
  void Initialize(RobotConfiguration configuration, RobotConnectionService robotConnectionService, AppStateRepository appStateRepository);
  boolean isMoving();
  void stop();

  void forward() throws InterruptedException;
  void forward(float dist_cm) throws InterruptedException;
  void backward() throws InterruptedException;
  void backward(float dist_cm) throws InterruptedException;
  void turn(int degrees) throws InterruptedException;

  void repeatWhileMoving(Callable repeatThis) throws InterruptedException;
  void waitWhileMoving() throws InterruptedException;
}
