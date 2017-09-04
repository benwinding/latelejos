package RobotRemote.Services.Listeners.Movement;

import RobotRemote.Helpers.Logger;
import RobotRemote.Helpers.Synchronizer;
import RobotRemote.Models.MotorsEnum;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.*;

public class MovePreciseThread {
  private LocationState locationState;
  private OdometryPoseProvider poseProvider;
  private Navigator navigator;

  MovePreciseThread(ArcRotateMoveController pilot, LocationState locationState, MovementState movementState) {
    this.locationState = locationState;
    this.navigator = this.CreateNavigator(pilot, locationState, movementState);
  }

  void moveToWaypoint(Waypoint waypoint) {
    // Needs work!
    Thread moveThread = new Thread(() -> Synchronizer.RunNotConcurrent(() -> {
      poseProvider.setPose(locationState.GetCurrentPose());
      navigator.goTo(waypoint);
      while(navigator.isMoving()) {
        // This blocks other communication to ev3 during navigator.goTo() call
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }));
    moveThread.start();
  }

  private Navigator CreateNavigator(ArcRotateMoveController pilot, LocationState locationState, MovementState movementState) {
    poseProvider = new OdometryPoseProvider(pilot);
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

  public void stop() {
    Synchronizer.RunNotConcurrent(() -> {
      if(this.navigator.isMoving())
        this.navigator.stop();
    });
  }
}
