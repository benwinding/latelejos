package RobotRemote.UI.Views;

import RobotRemote.Models.Enums.EnumCommandManual;
import RobotRemote.Models.Enums.EnumZoomCommand;
import RobotRemote.Models.MapPoint;
import RobotRemote.RobotStateMachine.Events.EventAutoControl;
import RobotRemote.RobotStateMachine.Events.ManualState.EventManualCommand;
import RobotRemote.RobotStateMachine.Events.Shared.EventEmergencySTOP;
import RobotRemote.RobotStateMachine.Events.Shared.EventSwitchToAutoMap;
import RobotRemote.RobotStateMachine.Events.Shared.EventSwitchToManual;
import RobotRemote.Shared.Logger;
import RobotRemote.Shared.ServiceManager;
import RobotRemote.UI.UiState;
import RobotRemote.UIServices.Events.EventUserAddNgz;
import RobotRemote.UIServices.Events.EventUserAddWaypoint;
import RobotRemote.UIServices.Events.EventUserMapDragged;
import RobotRemote.UIServices.Events.EventUserZoomChanged;
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
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lejos.robotics.navigation.Waypoint;

import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {
  public Pane map;
  public TextArea messageDisplayer;
  public Pane statusSensorUltra;
  public Pane statusSensorColour;
  public Pane statusIsConnected;
  public Pane locationDetails;
  public Pane sensorDisplay;

  @FXML
  RadioButton enterNgz;
  @FXML
  RadioButton enterWaypoint;
  @FXML
  Button btnManualMode;
  @FXML
  Button btnAutoSurveyMode;

  @FXML
  Button btnMoveUp;
  @FXML
  Button btnMoveDown;
  @FXML
  Button btnMoveLeft;
  @FXML
  Button btnMoveRight;
  @FXML
  Button btnMoveStop;
  @FXML
  TitledPane groupManualControls;

  private UiState uiState;
  private EventBus eventBus;

  // Variables for UI logic
  private MapPoint mapDragInitial = new MapPoint(0,0);

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Logger.log("UI Loaded!");
  }

  public void Init(ServiceManager sm) {
    this.uiState = sm.getAppState().getUiState();
    this.eventBus = sm.getEventBus();
    this.initMap();
    SetManualButtonsDisabled(true);
    //this.initManualMode();
  }

  private void initManualMode() {
    this.onClickManualMode(null);
  }

  private void initMap() {
    this.map.addEventHandler(ScrollEvent.SCROLL, event -> {
      double scrollAmount = event.getDeltaY();
      boolean isZoomIn = (scrollAmount > 0);
      eventBus.post(new EventUserZoomChanged(isZoomIn));
    });
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
        MoveMotors(EnumCommandManual.Halt);
        break;
      default:
        Logger.log("Key press:" + e.getCode() + " is not implemented");
    }
  }

  public void onClickManualMode(MouseEvent mouseEvent) {
    btnManualMode.setDisable(true);
    btnAutoSurveyMode.setDisable(false);
    eventBus.post(new EventEmergencySTOP());
    eventBus.post(new EventSwitchToManual());
    SetManualButtonsDisabled(false);
  }

  public void onClickAutoMapMode(MouseEvent mouseEvent) {
//    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Begin Automatic Survey of the Area?", ButtonType.YES, ButtonType.NO);
//    alert.showAndWait();
//    if (alert.getResult() == ButtonType.YES)
    btnManualMode.setDisable(false);
    btnAutoSurveyMode.setDisable(true);
    eventBus.post(new EventEmergencySTOP());
    eventBus.post(new EventSwitchToAutoMap());
    SetManualButtonsDisabled(true);
  }

  private void SetManualButtonsDisabled(boolean disabled) {
    btnMoveUp.setDisable(disabled);
    btnMoveDown.setDisable(disabled);
    btnMoveLeft.setDisable(disabled);
    btnMoveRight.setDisable(disabled);
  }

  public void onClickHelp(ActionEvent event) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RobotRemote/UI/Views/Help/HelpView.fxml"));
      Parent root = (Parent) fxmlLoader.load();
      Scene help = new Scene(root);
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
    this.SetManualButtonsDisabled(true);
    this.btnAutoSurveyMode.setDisable(false);
    this.btnManualMode.setDisable(false);
    eventBus.post(new EventEmergencySTOP());
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
    eventBus.post(new EventManualCommand(command));
  }

  private void StopMotors() {
    eventBus.post(new EventEmergencySTOP());
  }

  public void onClickZoomReset(MouseEvent mouseEvent) {
    eventBus.post(new EventUserZoomChanged(EnumZoomCommand.ZoomReset));
    mapDragInitial = new MapPoint(0,0);
    eventBus.post(new EventUserMapDragged(mapDragInitial.x,mapDragInitial.y));
  }

  public void onClickMap(MouseEvent mouseEvent) {
    if(mouseEvent.getButton() == MouseButton.SECONDARY) {
      mapDragInitial.x = mouseEvent.getX() - mapDragInitial.x;
      mapDragInitial.y = mouseEvent.getY() - mapDragInitial.y;
      Logger.log("UI: map drag start...");
    }
    else if(enterNgz.isSelected()) {
      this.eventBus.post(new EventUserAddNgz(mouseEvent.getX(), mouseEvent.getY()));
    }
    else if(enterWaypoint.isSelected()) {
      Waypoint gotoOnMap = new Waypoint(mouseEvent.getX(), mouseEvent.getY());
      eventBus.post(new EventUserAddWaypoint(gotoOnMap));
      eventBus.post(new EventAutoControl(gotoOnMap));
    }
  }

  public void onDragMap(MouseEvent dragEvent) {
    if(dragEvent.getButton() != MouseButton.SECONDARY)
      return;
    MapPoint dragDelta = new MapPoint(
        dragEvent.getX() - mapDragInitial.x,
        dragEvent.getY() - mapDragInitial.y
    );
    eventBus.post(new EventUserMapDragged(dragDelta.x,dragDelta.y));
  }

  public void onReleaseMap(MouseEvent mouseEvent) {
    MapPoint dragNew = new MapPoint(
        mouseEvent.getX() - mapDragInitial.x,
        mouseEvent.getY() - mapDragInitial.y
    );
    this.mapDragInitial = dragNew;
    Logger.log("UI: map drag end...");
  }
}
