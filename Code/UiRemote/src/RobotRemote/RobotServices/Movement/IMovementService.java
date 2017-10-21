package RobotRemote.RobotServices.Movement;

import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.Shared.AppStateRepository;
import RobotRemote.Shared.RobotConfiguration;
import lejos.robotics.navigation.Pose;

import java.util.concurrent.Callable;

public interface IMovementService {
  void Initialize(RobotConfiguration configuration, RobotConnectionService robotConnectionService, AppStateRepository appStateRepository);
  boolean isMoving();

  void gotoPoint(float x, float y) throws InterruptedException;

  void stop();
  void forward() throws InterruptedException;
  void forward(float dist_cm) throws InterruptedException;
  void backward() throws InterruptedException;
  void backward(float dist_cm) throws InterruptedException;
  void turn(double degrees) throws InterruptedException;

  void repeatWhileMoving(Callable repeatThis) throws InterruptedException;
  void waitWhileMoving() throws InterruptedException;
  Pose GetCurrentPose();
}
