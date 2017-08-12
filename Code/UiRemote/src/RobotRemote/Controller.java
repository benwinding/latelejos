package RobotRemote;

import javafx.scene.input.MouseEvent;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;

import java.rmi.RemoteException;

public class Controller {
  private RemoteEV3 BrickInstance;
  private RMIRegulatedMotor motorLeft;
  private RMIRegulatedMotor motorRight;

  public void onClickConnectToRobot(MouseEvent mouseEvent) {
    BrickInfo[] bricks = BrickFinder.discover();
    try {
      BrickInfo firstEv3 = bricks[0];
      this.BrickInstance = new RemoteEV3(firstEv3.getIPAddress());
      System.out.println("Found ev3!");
      System.out.println("Ip address: " + firstEv3.getIPAddress());
    }
    catch (Exception e) {
      System.out.println("No ev3 robots detected in network");
    }
    InitMotors();
  }

  private void InitMotors() {
    RemoteEV3 brick = this.BrickInstance;
    this.motorLeft = brick.createRegulatedMotor("A", 'L');
    this.motorRight = brick.createRegulatedMotor("B", 'L');
  }

  private void WriteMsg(String msg) {
    Brick brick = this.BrickInstance;
    brick.getTextLCD().clear();
    brick.getTextLCD().drawString(msg,0,4);
    System.out.println("Sending message:" + msg);
  }

  private void MoveMotors(String Direction) {
    try {
      motorLeft.setSpeed(200);
      motorRight.setSpeed(200);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    switch (Direction) {
      case "Forward" :
        try {
          motorLeft.forward();
          motorRight.forward();
        } catch (RemoteException e) {
          e.printStackTrace();
        }
        break;
      case "Backward" :
        try {
          motorLeft.backward();
          motorRight.backward();
        } catch (RemoteException e) {
          e.printStackTrace();
        }
        break;
      case "Left" :
        try {
          motorLeft.backward();
          motorRight.forward();
        } catch (RemoteException e) {
          e.printStackTrace();
        }
        break;
      case "Right" :
        try {
          motorLeft.forward();
          motorRight.backward();
        } catch (RemoteException e) {
          e.printStackTrace();
        }
        break;
      case "Stop" :
        try {
          motorLeft.close();
          motorRight.close();
        } catch (RemoteException e) {
          e.printStackTrace();
        }
        break;
      default:
        System.out.println("Unknown Direction: " + Direction);
    }
  }

  public void onClickStop(MouseEvent mouseEvent) {
    RemoteEV3 brick = this.BrickInstance;
    brick.getAudio().playTone(10,8);
    WriteMsg("STOPPING! ...");
    MoveMotors("Stop");
  }

  public void onClickForward(MouseEvent mouseEvent) throws RemoteException {
    WriteMsg("Moving Forward ...");
    MoveMotors("Forward");
  }

  public void onClickBackward(MouseEvent mouseEvent) {
    WriteMsg("Moving Backward ...");
    MoveMotors("Backward");
  }

  public void onClickLeft(MouseEvent mouseEvent) {
    WriteMsg("Moving Left ...");
    MoveMotors("Left");
  }

  public void onClickRight(MouseEvent mouseEvent) {
    WriteMsg("Moving Right ...");
    MoveMotors("Right");
  }
}
