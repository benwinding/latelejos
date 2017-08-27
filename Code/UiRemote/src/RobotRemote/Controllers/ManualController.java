package RobotRemote.Controllers;

import RobotRemote.Utils.Logger;
import RobotRemote.Models.MapState;
import RobotRemote.MapLayerFactory;
import RobotRemote.RobotMotorManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lejos.robotics.navigation.Pose;

import java.net.URL;
import java.util.ResourceBundle;

import static RobotRemote.RobotMotorManager.MoveMotors;

public class ManualController implements Initializable {
  private Scene help;
  private Scene demo;
  private MapState mapState;
  public static Thread mapRefreshThread = new Thread();

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

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Logger.Log("UI Loaded!");
  }

  public void Init(float initX, float initY) {
    this.mapState = new MapState(initX,initY);
    this.initGUI();
    this.initMap();
  }

  private void initMap() {
    Task task = new Task<Void>() {
      @Override
      public Void call() throws Exception {
        while (!isCancelled()) {
          Platform.runLater(new Runnable() {
            @Override
            public void run() {
              UpdateLocationFromRobot();
              Logger.LogCrossThread("Updating Map");
            }
          });
          Thread.sleep(500);
        }
        return null;
      }
    };
    mapRefreshThread = new Thread(task);
    mapRefreshThread.setDaemon(true);
    mapRefreshThread.start();
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
  }

  public void onClickDemo(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/views/DemoSensor.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            demo = new Scene(root, 700, 600);
            Stage stage = new Stage();
            stage.setTitle("Demo");
            stage.setScene(demo);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }

        Logger.demoInit(demo);

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
  }

  public void onClickForward(MouseEvent mouseEvent) {
    MoveMotors("Forward");
  }

  public void onClickBackward(MouseEvent mouseEvent) {
    MoveMotors("Backward");
  }

  public void onClickLeft(MouseEvent mouseEvent) {
    MoveMotors("Left");
  }

  public void onClickRight(MouseEvent mouseEvent) {
    MoveMotors("Right");
  }

  private void UpdateLocationFromRobot() {
    try {
      Pose pose = RobotMotorManager.GetCoords();
      mapState.AddPoint(pose.getX(), pose.getY(), pose.getHeading());
    } catch (Exception ignored) {
      Logger.Log("Unable to get Map location");
    }
    DrawMap();
  }

  private void DrawMap() {
    // Create map layers from mapstate
    MapLayerFactory mapFactory = new MapLayerFactory(mapState.GetMapSize());
    Canvas layerBorder = mapFactory.CreateBorderLayer(mapState.GetPointsBorder(), Color.BLACK);
    Canvas layerVisited = mapFactory.CreateVisitedLayer(mapState.GetPointsVisited(), Color.GREEN);
    Canvas layerRobot = mapFactory.CreateCurrentLocationLayer(mapState.GetLastPoint());
    Canvas layerSensors = mapFactory.CreateSensorFieldLayer(mapState.GetLastPoint());

    // Add to GUI
    map.getChildren().clear();
    map.getChildren().add(layerBorder);
    map.getChildren().add(layerSensors);
    map.getChildren().add(layerRobot);
    map.getChildren().add(layerVisited);
  }
}
