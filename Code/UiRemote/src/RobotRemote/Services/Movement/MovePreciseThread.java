package RobotRemote.Services.Movement;

import RobotRemote.Helpers.Synchronizer;
import RobotRemote.Services.RobotServiceBase;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;

public class MovePreciseThread extends RobotServiceBase {
  private LocationState locationState;
  private Navigator navigator;
  private Waypoint waypoint;

  MovePreciseThread(ArcRotateMoveController pilot, LocationState locationState, MovementState movementState) {
    super("Move Precisely", 50);
    this.locationState = locationState;
    this.navigator = NavigatorFactory.CreateNavigator(pilot, locationState, movementState);
  }

  void moveToWaypoint(Waypoint waypoint) {
    this.waypoint = waypoint;
    this.start();
  }

  @Override
  protected void Repeat() {
    Synchronizer.RunNotConcurrent(() -> {
      navigator.getPoseProvider().setPose(locationState.GetCurrentPose());
      navigator.goTo(waypoint);
      while(!thread.isInterrupted() && navigator.isMoving()) {
        // This blocks other communication to ev3 during navigator.goTo() call
        try {
          Thread.sleep(50);
        } catch (Exception ignored) {
        }
      }
    });
  }

  @Override
  protected void OnShutdown() {
    Synchronizer.RunNotConcurrent(() -> {
      if(this.navigator.isMoving())
        this.navigator.stop();
    });
  }
}
