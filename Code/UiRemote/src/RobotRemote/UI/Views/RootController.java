package RobotRemote.UI.Views;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.Enums.EnumCommandManual;
import RobotRemote.Models.Enums.EnumZoomCommand;
import RobotRemote.Models.Events.*;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Services.Connection.RobotConnectionService;
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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lejos.robotics.navigation.Waypoint;

import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {
  @FXML
  public Pane map;

  @FXML
  public AnchorPane rightSide;

  @FXML
  public TextArea messageDisplayer;

  @FXML
  public Pane locationDetails;

  @FXML
  public Pane sensorDisplay;

  @FXML
  RadioButton enterNgz;

  @FXML
  RadioButton enterWaypoint;

  @FXML
  ImageView status;

  @FXML
  Button switchmode;

  @FXML
  Button RobotMode;

  private RobotConfiguration config;
  private UiState uiState;
  private EventBus eventBus;
  private RobotConnectionService connectionService;
  private String state;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Logger.Log("UI Loaded!");
  }

  public void Init(RobotConfiguration config, UiState uiState, EventBus eventBus, RobotConnectionService connectionService) {
    this.config = config;
    this.uiState = uiState;
    this.eventBus = eventBus;
    this.connectionService = connectionService;
    this.state="Manual";
    this.initStatus();
    this.initMap();
    this.initSwitch();
  }

  private void initStatus() {
    if (connectionService.IsConnected())
      this.status.setImage(new Image("RobotRemote/UI/Images/status_green.png"));
    else
      this.status.setImage(new Image("RobotRemote/UI/Images/status_red.png"));
  }

  private void initMap() {
    this.map.addEventHandler(ScrollEvent.SCROLL, event -> {
      double scrollAmount = event.getDeltaY();
      if(scrollAmount > 0)
        eventBus.post(new EventUserZoomChanged(EnumZoomCommand.IncrementZoom));
      else
        eventBus.post(new EventUserZoomChanged(EnumZoomCommand.DecrementZoom));
    });
  }

  private void initSwitch() {
    this.switchmode.setText("Switch Mode");
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
    if(state == "Auto") {
       // this.switchmode.setText("Manual");
      state="Manual";
        this.RobotMode.setText("Manual");
    }
    else{
      //this.switchmode.setText("Auto");
      state="Auto";
      this.RobotMode.setText("Auto");
    }
    eventBus.post(new EventRobotmode());
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
  }

  public void onClickZoomReset(MouseEvent mouseEvent) {
    eventBus.post(new EventUserZoomChanged(EnumZoomCommand.ZoomReset));
  }
}
