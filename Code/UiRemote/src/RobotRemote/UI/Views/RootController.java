package RobotRemote.UI.Views;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.EnumCommandManual;
import RobotRemote.Models.Events.*;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Services.Listeners.Connection.RobotConnectionService;
import RobotRemote.UI.UiState;
import com.google.common.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lejos.robotics.navigation.Waypoint;

import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {
  @FXML
  public Pane map;

  @FXML
  public TextArea messageDisplayer;

  @FXML
  public Pane locationDetails;

  @FXML
  public Pane sensorDisplay;

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
  RadioButton enterNgz;

  @FXML
  RadioButton enterWaypoint;

  @FXML
  RadioButton zoomMapIn;

  @FXML
  RadioButton zoomMapOut;

  @FXML
  ImageView status;

  @FXML
  Button switchmode;

  private UiState uiState;
  private EventBus eventBus;
  private RobotConnectionService connectionService;
  private int counter=0;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Logger.Log("UI Loaded!");
  }

  public void Init(RobotConfiguration config, UiState uiState, EventBus eventBus, RobotConnectionService connectionService) {
    this.uiState = uiState;
    this.eventBus = eventBus;
    this.connectionService =connectionService;
    this.initGUI();
  }

  private void initGUI() {
    this.btnMoveLeft.setImage(new Image("RobotRemote/UI/Images/left.png"));
    this.btnMoveRight.setImage(new Image("RobotRemote/UI/Images/right.png"));
    this.btnMoveUp.setImage(new Image("RobotRemote/UI/Images/up.png"));
    this.btnMoveDown.setImage(new Image("RobotRemote/UI/Images/down.png"));
    this.btnMoveStop.setImage(new Image("RobotRemote/UI/Images/stop.png"));
    this.switchmode.setText("Manual");
    if (connectionService.IsConnected()) this.status.setImage(new Image("RobotRemote/UI/Images/status_green.png"));
    else this.status.setImage(new Image("RobotRemote/UI/Images/status_red.png"));


  }

  public void keyPressed(KeyEvent e) {
    switch (e.getCode()) {
      case W:
        MoveMotors(EnumCommandManual.Forward);
        break;
      case A:
        MoveMotors(EnumCommandManual.Left);
        break;
      case D:
        MoveMotors(EnumCommandManual.Right);
        break;
      case S:
        MoveMotors(EnumCommandManual.Backward);
        break;
      case ENTER:
      case SPACE:
      case Q:
        MoveMotors(EnumCommandManual.Stop);
        break;
      default:
        Logger.Log("Key press:" + e.getCode() + " is not implemented");
    }
  }

  public void onClickSwitch(MouseEvent mouseEvent){
    if(counter%2==0) this.switchmode.setText("Auto");
    else this.switchmode.setText("Manual");
    counter++;
    eventBus.post(new EventRobotmode());

  }

  public void onClickDemo(MouseEvent mouseEvent) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RobotRemote/UI/Views/Demo/DemoSensor.fxml"));
      Parent root = (Parent) fxmlLoader.load();
      Scene demo = new Scene(root, 700, 600);
      Stage stage = new Stage();
      stage.setTitle("Demo");
      stage.setScene(demo);
      stage.show();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void onClickHelp(ActionEvent event) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RobotRemote/UI/Views/Help/HelpView.fxml"));
      Parent root = (Parent) fxmlLoader.load();
      Scene help = new Scene(root, 400, 300);
      Stage stage = new Stage();
      stage.setTitle("Getting Started");
      stage.setScene(help);
      stage.show();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void onClickAbout(ActionEvent event) {

    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RobotRemote/UI/Views/About/About.fxml"));
      Parent root = (Parent) fxmlLoader.load();
      Scene help = new Scene(root, 400, 300);
      Stage stage = new Stage();
      stage.setTitle("About");
      stage.setScene(help);
      stage.show();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void onClickStop(MouseEvent mouseEvent) {
    MoveMotors(EnumCommandManual.Stop);
  }

  public void onClickForward(MouseEvent mouseEvent) {
    MoveMotors(EnumCommandManual.Forward);
  }

  public void onClickBackward(MouseEvent mouseEvent) {
    MoveMotors(EnumCommandManual.Backward);
  }

  public void onClickLeft(MouseEvent mouseEvent) {
    MoveMotors(EnumCommandManual.Left);
  }

  public void onClickRight(MouseEvent mouseEvent) {
    MoveMotors(EnumCommandManual.Right);
  }

  private void MoveMotors(EnumCommandManual command) {
    uiState.setCurrentCommand(command);
    eventBus.post(new EventManualControl(command));
  }

  public void onClickMap(MouseEvent mouseEvent) {
    if(enterNgz.isSelected()) {
      Logger.LogCrossThread("Event: Mouse click being posted");
      this.eventBus.post(new EventUserAddNgz(mouseEvent.getX(), mouseEvent.getY()));
    }
    else if(enterWaypoint.isSelected()) {
      uiState.setCurrentCommand(EnumCommandManual.MoveToPrecise);
      Waypoint gotoOnMap = new Waypoint(mouseEvent.getX(), mouseEvent.getY());
      eventBus.post(new EventUserAddWaypoint(gotoOnMap));
      eventBus.post(new EventAutoControl(gotoOnMap));
    }
    else if(zoomMapIn.isSelected()) {
      eventBus.post(new EventUserZoomChanged(true));
    }
    else if(zoomMapOut.isSelected()) {
      eventBus.post(new EventUserZoomChanged(false));
    }
  }
}
