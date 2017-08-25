package RobotRemote;

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
import java.util.List;

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
      UpdateFromRobotLocation();
    }

    else if(e.getCode()==KeyCode.A){
      MoveMotors("Left");
      UpdateFromRobotLocation();
    }

    else if(e.getCode()==KeyCode.D){
      MoveMotors("Right");
      UpdateFromRobotLocation();
    }

    else if(e.getCode()==KeyCode.S){
      MoveMotors("Backward");
      UpdateFromRobotLocation();
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
    UpdateFromRobotLocation();
  }

  public void onClickForward(MouseEvent mouseEvent) throws RemoteException {
    MoveMotors("Forward");
    UpdateFromRobotLocation();
  }

  public void onClickBackward(MouseEvent mouseEvent) {
    MoveMotors("Backward");
    UpdateFromRobotLocation();
  }

  public void onClickLeft(MouseEvent mouseEvent) {
    MoveMotors("Left");
    UpdateFromRobotLocation();
  }

  public void onClickRight(MouseEvent mouseEvent) {
    MoveMotors("Right");
    UpdateFromRobotLocation();
  }

  private void UpdateFromRobotLocation() {
    Pose robotPose = RobotMotorManager.GetCoords();
    if(robotPose == null) {
      Logger.Log("Unable to get Map location");
      return;
    }
    SyncMapToView(robotPose.getX(), robotPose.getY());
  }

  private void SyncMapToView(double x, double y) {
    mapState.AddPoint(x,y);
    List<MapPoint> points = mapState.GetPointsVisited();

    Canvas canvas = (Canvas) this.scene.lookup("#robotMap");
    GraphicsContext mapUi = canvas.getGraphicsContext2D();

    for(int i = 0; i<points.size() - 1;i++) {
      MapPoint p1 = points.get(i);
      MapPoint p2 = points.get(i+1);
      mapUi.strokeLine(p1.x, p1.y, p2.x, p2.y);
    }
  }
}
