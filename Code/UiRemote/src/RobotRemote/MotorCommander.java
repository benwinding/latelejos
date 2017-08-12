package RobotRemote;

import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;

import java.rmi.RemoteException;

public class MotorCommander {
  private static RMIRegulatedMotor motorLeft;
  private static RMIRegulatedMotor motorRight;

  public static void ShutdownMotors() {
    try {
      motorLeft.close();
      motorRight.close();
      System.out.println("Successfully closed motor ports");
    } catch (RemoteException e) {
      System.out.println("Unable to close motor ports");
    } catch (NullPointerException e) {
      System.out.println("Unable to close motor ports, motors never connected to");
    }
  }

  public static void InitMotors() {
    RemoteEV3 brick = ConnectionManager.GetBrick();
    try {
      motorLeft = brick.createRegulatedMotor("A", 'L');
      motorRight = brick.createRegulatedMotor("B", 'L');
      System.out.println("Successfully opened motor ports");
    } catch (Exception e) {
      System.out.println("Unable to open motor ports");
    }
  }

  static void MoveMotors(String Direction) {
    try {
      motorLeft.setSpeed(200);
      motorRight.setSpeed(200);
    } catch (RemoteException e) {
      System.out.println("Unable to set motor speed");
    }
    switch (Direction) {
      case "Forward" :
        try {
          motorLeft.forward();
          motorRight.forward();
        } catch (RemoteException e) {
          System.out.println("Unable to set motors: " + Direction);
        }
        break;
      case "Backward" :
        try {
          motorLeft.backward();
          motorRight.backward();
        } catch (RemoteException e) {
          System.out.println("Unable to set motors: " + Direction);
        }
        break;
      case "Left" :
        try {
          motorLeft.backward();
          motorRight.forward();
        } catch (RemoteException e) {
          System.out.println("Unable to set motors: " + Direction);
        }
        break;
      case "Right" :
        try {
          motorLeft.forward();
          motorRight.backward();
        } catch (RemoteException e) {
          System.out.println("Unable to set motors: " + Direction);
        }
        break;
      case "Stop" :
        try {
          motorLeft.close();
          motorRight.close();
        } catch (RemoteException e) {
          System.out.println("Unable to set motors: " + Direction);
        }
        break;
      default:
        System.out.println("Unknown Direction: " + Direction);
    }
  }
}
