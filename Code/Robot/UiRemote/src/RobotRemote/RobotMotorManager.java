package RobotRemote;

import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;

import java.rmi.RemoteException;

public class RobotMotorManager {
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
    RemoteEV3 brick = RobotConnectionManager.GetBrick();
    try {
      motorLeft = brick.createRegulatedMotor("A", 'L');
      motorRight = brick.createRegulatedMotor("B", 'L');
      System.out.println("Successfully opened motor ports");
    } catch (Exception e) {
      System.out.println("Unable to open motor ports");
    }
  }

  static void SetMotorSpeed(int speed) {
    try {
      motorLeft.setSpeed(speed);
      motorRight.setSpeed(speed);
    } catch (Exception e) {
      System.out.println("Unable to set motor speed");
    }
  }

  static void MoveMotors(String Direction) {
    switch (Direction) {
      case "Forward" :
        SetMotorSpeed(200);
        MoveLeftMotor(true);
        MoveRightMotor(true);
        break;
      case "Backward" :
        SetMotorSpeed(200);
        MoveLeftMotor(false);
        MoveRightMotor(false);
        break;
      case "Left" :
        SetMotorSpeed(100);
        MoveLeftMotor(false);
        MoveRightMotor(true);
        break;
      case "Right" :
        SetMotorSpeed(100);
        MoveLeftMotor(true);
        MoveRightMotor(false);
        break;
      case "Stop" :
        try {
          motorLeft.stop(true);
          motorRight.stop(true);
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
