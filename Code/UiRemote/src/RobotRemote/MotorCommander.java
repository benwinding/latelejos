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
    } catch (Exception e) {
      System.out.println("Unable to set motor speed");
    }
    switch (Direction) {
      case "Forward" :
        MoveLeftMotor(true);
        MoveRightMotor(true);
        break;
      case "Backward" :
        MoveLeftMotor(false);
        MoveRightMotor(false);
        break;
      case "Left" :
        MoveLeftMotor(false);
        MoveRightMotor(true);
        break;
      case "Right" :
        MoveLeftMotor(true);
        MoveRightMotor(false);
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

  static void MoveLeftMotor(boolean forward) {
    try {
      if(forward)
        motorLeft.forward();
      else
        motorLeft.backward();
    } catch (Exception e) {
      System.out.println("Unable to move left motor");
    }
  }

  static void MoveRightMotor(boolean forward) {
    try {
      if(forward)
        motorRight.forward();
      else
        motorRight.backward();
    } catch (Exception e) {
      System.out.println("Unable to move right motor");
    }
  }
}