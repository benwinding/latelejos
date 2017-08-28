package RobotRemote.Controllers;

import RobotRemote.MapLayerFactory;
import RobotRemote.Mocks.TestingMotorManager;
import RobotRemote.Models.MapState;
import RobotRemote.RobotMotorManager;
import RobotRemote.Utils.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lejos.robotics.navigation.Pose;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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

  @FXML
  CheckBox isTestControls;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Logger.Log("UI Loaded!");
  }

  public void Init(float initX, float initY, float initTheta) {
    this.mapState = new MapState(initX,initY,initTheta);
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
      case UP:
        MoveMotors("Forward");
        break;
      case A:
      case LEFT:
        MoveMotors("Left");
        break;
      case D:
      case RIGHT:
        MoveMotors("Right");
        break;
      case S:
      case DOWN:
        MoveMotors("Backward");
        break;
        case ENTER:
            MoveMotors("Stop");
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

  private void MoveMotors(String command) {
    if(isTestControls.isSelected()) {
      TestingMotorManager.MoveMotors(command);
    }
    else {
      RobotMotorManager.MoveMotors(command);
    }
  }

  private void UpdateLocationFromRobot() {
    try {
      Pose pose;
      if(isTestControls.isSelected()) {
        pose = TestingMotorManager.GetCoords();
      }
      else {
        pose = RobotMotorManager.GetCoords();
      }
      mapState.AddPoint(pose.getX(), pose.getY(), pose.getHeading());
    } catch (Exception ignored) {
      Logger.Log("Warning: Unable to get Map location");
    }
    DrawMap();
  }

  private void DrawMap() {
    // Create map layers from mapstate
    MapLayerFactory mapFactory = new MapLayerFactory(mapState);
    List<Canvas> allMapLayers = mapFactory.CreateMapLayers();
    // Add to GUI
    map.getChildren().clear();
    map.getChildren().addAll(allMapLayers);
  }
}
