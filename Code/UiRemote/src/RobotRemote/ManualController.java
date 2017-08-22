package RobotRemote;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import lejos.hardware.Brick;
import lejos.remote.ev3.RemoteEV3;
import java.rmi.RemoteException;
import static RobotRemote.RobotMotorManager.InitMotors;
import static RobotRemote.RobotMotorManager.MoveMotors;

public class ManualController {
  private Scene scene;

  public void onClickConnectToRobot(MouseEvent mouseEvent) {
    InitMotors();
  }

  private void WriteMsg(String msg) {
    Brick brick = RobotConnectionManager.GetBrick();
    WriteToGUI(msg);
    try {
      brick.getTextLCD().clear();
      brick.getTextLCD().drawString(msg,0,4);
      System.out.println("Sending message:" + msg);
    } catch(Exception e) {
      System.out.println("Could not write to LCD");
    }
  }

  private void WriteToGUI(String msg) {
    TextArea textArea = (TextArea) this.scene.lookup("#messageDisplayer");
    textArea.appendText(msg + '\n');
  }

  public void onClickStop(MouseEvent mouseEvent) {
    RemoteEV3 brick = RobotConnectionManager.GetBrick();
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

  public void onClickMap(MouseEvent mouseEvent) throws Exception {
    System.out.println("Clicked map: x=" + mouseEvent.getX() + ",y="+ mouseEvent.getY());
    Canvas canvas = (Canvas) this.scene.lookup("#robotMap");
    GraphicsContext a = canvas.getGraphicsContext2D();
    a.strokeLine(0,0,mouseEvent.getX(), mouseEvent.getY());
  }

  public void setScene(Scene scene) {
    this.scene = scene;
  }
}
