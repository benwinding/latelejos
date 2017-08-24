package RobotRemote;

import lejos.remote.ev3.RemoteRequestEV3;
import lejos.robotics.geometry.Point;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;

import static lejos.robotics.navigation.MoveController.WHEEL_SIZE_EV3;

class RobotMotorManager {
  private static ArcRotateMoveController pilot;
  private static Navigator navigator;
  static Pose pose;
  private static RobotCoordinateSystemInterface cs;

  static void InitMotors() {
    RemoteRequestEV3 brick = RobotConnectionManager.GetBrick();
    try {
      pilot = brick.createPilot(WHEEL_SIZE_EV3,10,"A","B");
      pilot.setLinearSpeed(5);
      pilot.setLinearAcceleration(5);
      pilot.setAngularSpeed(30);
      pilot.setAngularAcceleration(10);

      navigator = new Navigator(pilot);
      pose = navigator.getPoseProvider().getPose();
      //MoveInSquare(20);

      System.out.println("Successfully opened motor ports");
    } catch (Exception e) {
      System.out.println("Unable to open motor ports");
    }
  }

  private static void MoveInSquare(int size) {
    navigator.goTo(0,0);
    navigator.goTo(size,0);
    navigator.goTo(0,size);
    navigator.goTo(0,size);
    navigator.goTo(0,size);
    navigator.goTo(0,0);
  }

  static void MoveMotors(String Direction) {
    switch (Direction) {
      case "Forward":
        navigator.goTo(10,0);
        pose.translate(10,0);
        break;
      case "Backward":
        navigator.goTo(-10,0);
        pose.translate(-10,0);
        break;
      case "Left":
        navigator.goTo(0,10);
        pose.translate(0,10);
        break;
      case "Right":
        navigator.goTo(0,-10);
        pose.translate(0,-10);
        break;
      case "Stop":
        pilot.stop();
        break;
      default:
        System.out.println("Unknown Direction: " + Direction);
    }
  }

  static Pose GetCoords() {
    return cs.GetGlobalPose();
  }
}
