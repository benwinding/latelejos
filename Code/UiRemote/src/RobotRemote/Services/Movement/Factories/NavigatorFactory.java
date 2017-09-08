package RobotRemote.Services.Movement.Factories;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.MotorsEnum;
import RobotRemote.Services.Movement.MoveThreads.LocationState;
import RobotRemote.Services.Movement.MoveThreads.MovementState;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.*;

public class NavigatorFactory {
  public static Navigator CreateNavigator(ArcRotateMoveController pilot, LocationState locationState, MovementState movementState) {
    OdometryPoseProvider poseProvider = new OdometryPoseProvider(pilot);
    poseProvider.setPose(locationState.GetCurrentPose());
    Navigator navigator = new Navigator(pilot, poseProvider);
    navigator.addNavigationListener(new NavigationListener() {
      @Override
      public void atWaypoint(Waypoint waypoint, Pose pose, int i) {
        movementState.setMotorState(MotorsEnum.PathAtWayPoint);
        Logger.log(String.format("NAV: At way point:: x:%.1f, y:%.1f, θ:%.0f", waypoint.getX(), waypoint.getY(), waypoint.getHeading()));
      }

      @Override
      public void pathComplete(Waypoint waypoint, Pose pose, int i) {
        movementState.setMotorState(MotorsEnum.PathComplete);
        float heading = pose.angleTo(waypoint);
        Pose newPose = new Pose((float)waypoint.getX(), (float)waypoint.getY(), heading);
        Logger.log(String.format("NAV: complete, way point:: x:%.1f, y:%.1f, θ:%.0f", newPose.getX(), newPose.getY(), newPose.getHeading()));
        poseProvider.setPose(newPose);
        locationState.GoingToPose(newPose);
      }

      @Override
      public void pathInterrupted(Waypoint waypoint, Pose pose, int i) {
        movementState.setMotorState(MotorsEnum.PathInterupted);
        Logger.log(String.format("NAV: Interrupt at way point:: x:%.1f, y:%.1f, θ:%.0f", waypoint.getX(), waypoint.getY(), waypoint.getHeading()));
        float heading = pose.angleTo(waypoint);
        Pose newPose = new Pose((float)waypoint.getX(), (float)waypoint.getY(), heading);
        poseProvider.setPose(newPose);
        locationState.GoingToPose(newPose);
      }
    });
    return navigator;
  }

}
