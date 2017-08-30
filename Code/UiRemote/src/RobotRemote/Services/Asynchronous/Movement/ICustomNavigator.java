package RobotRemote.Services.Asynchronous.Movement;

import RobotRemote.Services.Synchronous.Connection.RobotConnectionService;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Pose;

public interface ICustomNavigator {
  void Init(ICustomCoordinateSystem cs, ArcRotateMoveController pilot, RobotConnectionService robotConnectionService);
  void MoveStraight(float distance);
  void MoveAsync(boolean ...backward);
  void Rotate(float angle);
  void Stop();

  Pose GetGlobalPose();
}
