package RobotRemote.Models.Interfaces;

import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Pose;

public interface CustomNavigatorInterface {
  void Init(RobotCoordinateSystemInterface cs, ArcRotateMoveController pilot);
  void MoveStraight(float distance);
  void MoveAsync(boolean ...backward);
  void Rotate(float angle);
  void Stop();

  Pose GetGlobalPose();
}
