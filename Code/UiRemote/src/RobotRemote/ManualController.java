package RobotRemote;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import lejos.robotics.navigation.Pose;

import java.rmi.RemoteException;
import java.util.List;

import static RobotRemote.RobotMotorManager.MoveMotors;

public class ManualController {
  private Scene scene;
  private static MapState mapState = new MapState(100,100);

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

  public void onClickMap(MouseEvent mouseEvent) throws Exception {
    Logger.Log("Clicked map: x=" + mouseEvent.getX() + ",y="+ mouseEvent.getY());
  }

  private void UpdateFromRobotLocation() {
    Pose robotPose = RobotMotorManager.GetCoords();
    if(robotPose == null) {
      Logger.Log("Unable to get Map location");
      return;
    }
    SyncMapToView(robotPose.getX()+100, robotPose.getY()+100);
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

  void setScene(Scene scene) {
    this.scene = scene;
  }
}
