package RobotRemote;

import javafx.scene.paint.Color;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lejos.robotics.navigation.Pose;

import java.rmi.RemoteException;

import static RobotRemote.RobotMotorManager.MoveMotors;

public class ManualController {
  private Scene scene;
  private Scene help;
  private MapState mapState;

  public void Init(Scene scene, float initX, float initY) {
    this.scene = scene;
    this.mapState = new MapState(initX,initY);
  }

  public void keyPressed(KeyEvent e) {
    if(e.getCode()==KeyCode.W){
      MoveMotors("Forward");
      UpdateLocationFromRobot();
    }

    else if(e.getCode()==KeyCode.A){
      MoveMotors("Left");
      UpdateLocationFromRobot();
    }

    else if(e.getCode()==KeyCode.D){
      MoveMotors("Right");
      UpdateLocationFromRobot();
    }

    else if(e.getCode()==KeyCode.S){
      MoveMotors("Backward");
      UpdateLocationFromRobot();
    }
  }

  public void onClickHelp(MouseEvent mouseEvent) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HelpView.fxml"));
      Parent root = (Parent) fxmlLoader.load();
      help = new Scene(root, 400, 300);
      Stage stage = new Stage();
      stage.setTitle("Help Menu");
      stage.setScene(help);
      stage.show();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void onClickStop(MouseEvent mouseEvent) {
    MoveMotors("Stop");
    UpdateLocationFromRobot();
  }

  public void onClickForward(MouseEvent mouseEvent) throws RemoteException {
    MoveMotors("Forward");
    UpdateLocationFromRobot();
  }

  public void onClickBackward(MouseEvent mouseEvent) {
    MoveMotors("Backward");
    UpdateLocationFromRobot();
  }

  public void onClickLeft(MouseEvent mouseEvent) {
    MoveMotors("Left");
    UpdateLocationFromRobot();
  }

  public void onClickRight(MouseEvent mouseEvent) {
    MoveMotors("Right");
    UpdateLocationFromRobot();
  }

  private void UpdateLocationFromRobot() {
    Pose robotPose = RobotMotorManager.GetCoords();
    if(robotPose == null) {
      Logger.Log("Unable to get Map location");
      return;
    }
    mapState.AddPoint(robotPose.getX(),robotPose.getY());
    SyncMapStateToView();
  }

  private void SyncMapStateToView() {
    Canvas canvas = (Canvas) this.scene.lookup("#robotMap");
    GraphicsContext mapUi = canvas.getGraphicsContext2D();

    MapUiDrawer.DrawPoints(mapUi, mapState.GetPointsBorder(), Color.BLACK);
    MapUiDrawer.DrawPoints(mapUi, mapState.GetMapSize(), mapState.GetPointsVisited(), Color.GREEN);
  }
}
