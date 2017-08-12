package RobotRemote;

import javafx.scene.input.MouseEvent;
import lejos.hardware.Brick;
import lejos.remote.ev3.RemoteEV3;
import java.rmi.RemoteException;

import static RobotRemote.MotorCommander.InitMotors;
import static RobotRemote.MotorCommander.MoveMotors;

public class Controller {
  public void onClickConnectToRobot(MouseEvent mouseEvent) {
    InitMotors();
  }


  private static void WriteMsg(String msg) {
    Brick brick = ConnectionManager.GetBrick();
    brick.getTextLCD().clear();
    brick.getTextLCD().drawString(msg,0,4);
    System.out.println("Sending message:" + msg);
  }

  public void onClickStop(MouseEvent mouseEvent) {
    RemoteEV3 brick = ConnectionManager.GetBrick();
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
