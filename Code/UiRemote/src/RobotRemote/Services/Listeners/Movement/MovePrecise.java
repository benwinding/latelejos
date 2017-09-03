package RobotRemote.Services.Listeners.Movement;

import RobotRemote.Helpers.Logger;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.*;

public class MovePrecise {
  private final Navigator navigator;

  MovePrecise(ArcRotateMoveController pilot, LocationState uiState, MovementState movementState) {
    this.navigator = this.CreateNavigator(pilot, uiState, movementState);
  }

  void moveToWaypoint(Waypoint waypoint) {
    this.navigator.goTo(waypoint);
  }

  private static Navigator CreateNavigator(ArcRotateMoveController pilot, LocationState locationState, MovementState movementState) {
    PoseProvider poseProvider = new OdometryPoseProvider(pilot);
    Navigator navigator = new Navigator(pilot, poseProvider);
    navigator.addNavigationListener(new NavigationListener() {
      @Override
      public void atWaypoint(Waypoint waypoint, Pose pose, int i) {
        Logger.LogCrossThread("NAV: At waypoint, x:" + waypoint.getX() + ", y:" + waypoint.getY());
        locationState.GoingToWayPoint(waypoint);
        pose.setLocation((float) waypoint.getX(), (float) waypoint.getY());
        pose.setHeading((float) waypoint.getHeading());
        poseProvider.setPose(pose);
      }

      @Override
      public void pathComplete(Waypoint waypoint, Pose pose, int i) {
        Logger.LogCrossThread("NAV: complete, way point , x:" + waypoint.getX() + ", y:" + waypoint.getY());
        locationState.GoingToWayPoint(waypoint);
        pose.setLocation((float) waypoint.getX(), (float) waypoint.getY());
        pose.setHeading((float) waypoint.getHeading());
        poseProvider.setPose(pose);
      }

      @Override
      public void pathInterrupted(Waypoint waypoint, Pose pose, int i) {
        Logger.LogCrossThread("NAV: interrupt at waypoint, x:" + waypoint.getX() + ", y:" + waypoint.getY());
        locationState.GoingToWayPoint(waypoint);
        pose.setLocation((float) waypoint.getX(), (float) waypoint.getY());
        pose.setHeading((float) waypoint.getHeading());
        poseProvider.setPose(pose);
      }
    });
    return navigator;
  }

  public void stop() {
    if(this.navigator.isMoving())
      this.navigator.stop();
  }
}
