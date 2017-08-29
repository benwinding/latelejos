package RobotRemote.Services;

import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Pose;

public interface ICustomNavigator {
  void Init(ICustomCoordinateSystem cs, ArcRotateMoveController pilot);
  void MoveStraight(float distance);
  void MoveAsync(boolean ...backward);
  void Rotate(float angle);
  void Stop();

  Pose GetGlobalPose();
}
