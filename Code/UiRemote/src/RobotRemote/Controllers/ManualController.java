package RobotRemote.Controllers;

import RobotRemote.Utils.Logger;
import RobotRemote.Models.MapState;
import RobotRemote.MapUiDrawer;
import RobotRemote.RobotMotorManager;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lejos.robotics.navigation.Pose;

import static RobotRemote.RobotMotorManager.MoveMotors;

public class ManualController {
  private Scene scene;
  private Scene help;
  private MapState mapState;
  @FXML
  ImageView btnMoveLeft;

  @FXML
  ImageView btnMoveUp;

  @FXML
  ImageView btnMoveDown;

  @FXML
  ImageView btnMoveRight;

  @FXML
  ImageView btnMoveStop;

  @FXML
  Pane map;

  public void Init(Scene scene, float initX, float initY) {
    this.scene = scene;
    this.mapState = new MapState(initX,initY);
    this.initGUI();
  }

  private void initGUI(){

      this.btnMoveLeft.setImage(new Image("res/img/left.png"));
      this.btnMoveRight.setImage(new Image("res/img/right.png"));
      this.btnMoveUp.setImage(new Image("res/img/up.png"));
      this.btnMoveDown.setImage(new Image("res/img/down.png"));
      this.btnMoveStop.setImage(new Image("res/img/stop.png"));
  }
  public void keyPressed(KeyEvent e) {
    switch (e.getCode()) {
      case W:
        MoveMotors("Forward");
        break;
      case A:
        MoveMotors("Left");
        break;
      case D:
        MoveMotors("Right");
        break;
      case S:
        MoveMotors("Backward");
        break;
      default:
        Logger.Log("Key press:" + e.getCode() + " is not implemented");
    }
    UpdateLocationFromRobot();
  }

  public void onClickHelp(MouseEvent mouseEvent) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/views/HelpView.fxml"));
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

  public void onClickForward(MouseEvent mouseEvent) {
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
    Pose pose = RobotMotorManager.GetCoords();
    if(pose == null) {
      Logger.Log("Unable to get Map location");
      return;
    }
    mapState.AddPoint(pose.getX(), pose.getY(), pose.getHeading());
    SyncMapStateToView();
  }

  private void SyncMapStateToView() {
    // Create Layers
    Canvas layer1 = new Canvas(500,400);
    Canvas layer2 = new Canvas(500,400);
    Canvas layer3 = new Canvas(500,400);

    // Obtain Graphics Contexts
    GraphicsContext layerBorder = layer1.getGraphicsContext2D();
    GraphicsContext layerPointsVisited = layer2.getGraphicsContext2D();
    GraphicsContext layerRobot = layer3.getGraphicsContext2D();

    // Draw on layers
    MapUiDrawer mapDrawer = new MapUiDrawer(mapState.GetMapSize());
    mapDrawer.DrawBorderPoints(layerBorder, mapState.GetPointsBorder(), Color.BLACK);
    mapDrawer.DrawMapPoints(layerPointsVisited, mapState.GetPointsVisited(), Color.GREEN);
    mapDrawer.DrawRobot(layerRobot, mapState.GetLastPoint());

    // Add to GUI
    map.getChildren().clear();
    map.getChildren().add(layer1);
    map.getChildren().add(layer2);
    map.getChildren().add(layer3);
  }
}
