package RobotRemote.Services.Listeners.Movement;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.MotorsEnum;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.*;

public class NavigatorFactory {
  static Navigator CreateNavigator(ArcRotateMoveController pilot, LocationState locationState, MovementState movementState) {
    OdometryPoseProvider poseProvider = new OdometryPoseProvider(pilot);
    poseProvider.setPose(locationState.GetCurrentPose());
    Navigator navigator = new Navigator(pilot, poseProvider);
    navigator.addNavigationListener(new NavigationListener() {
      @Override
      public void atWaypoint(Waypoint waypoint, Pose pose, int i) {
        movementState.setMotorState(MotorsEnum.PathAtWayPoint);
        Logger.LogCrossThread("NAV: At waypoint, x:" + waypoint.getX() + ", y:" + waypoint.getY());
        locationState.GoingToWayPoint(waypoint);
        pose.setLocation((float) waypoint.getX(), (float) waypoint.getY());
        pose.setHeading((float) waypoint.getHeading());
        poseProvider.setPose(pose);
      }

      @Override
      public void pathComplete(Waypoint waypoint, Pose pose, int i) {
        movementState.setMotorState(MotorsEnum.PathComplete);
        Logger.LogCrossThread("NAV: complete, way point , x:" + waypoint.getX() + ", y:" + waypoint.getY());
        locationState.GoingToWayPoint(waypoint);
        pose.setLocation((float) waypoint.getX(), (float) waypoint.getY());
        pose.setHeading((float) waypoint.getHeading());
        poseProvider.setPose(pose);
      }

      @Override
      public void pathInterrupted(Waypoint waypoint, Pose pose, int i) {
        movementState.setMotorState(MotorsEnum.PathInterupted);
        Logger.LogCrossThread("NAV: interrupt at waypoint, x:" + waypoint.getX() + ", y:" + waypoint.getY());
        locationState.GoingToWayPoint(waypoint);
        pose.setLocation((float) waypoint.getX(), (float) waypoint.getY());
        pose.setHeading((float) waypoint.getHeading());
        poseProvider.setPose(pose);
      }
    });
    return navigator;
  }

}
