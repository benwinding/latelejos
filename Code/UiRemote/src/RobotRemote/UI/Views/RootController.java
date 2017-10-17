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
import RobotRemote.UIServices.Events.*;
import com.google.common.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lejos.robotics.navigation.Waypoint;

import java.io.File;
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
  VBox infoNgzSelction;

  private UiState uiState;
  private EventBus eventBus;

  // Variables for UI logic
  private MapPoint mapDragInitial = new MapPoint(0,0);
  private boolean isFirstNgzPoint = true;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Logger.debug("UI Loaded!");
  }

  public void Init(ServiceManager sm) {
    this.uiState = sm.getAppState().getUiState();
    this.eventBus = sm.getEventBus();
    this.initMap();
    this.infoNgzSelction.setVisible(false);
    SetManualButtonsDisabled(true);
  }

  private void initMap() {
    this.map.addEventHandler(ScrollEvent.SCROLL, event -> {
      double scrollAmount = event.getDeltaY();
      boolean isZoomIn = (scrollAmount > 0);
      eventBus.post(new EventUserZoomChanged(isZoomIn));
    });
  }

  public void keyPressed(KeyEvent e) {
    KeyCode a = e.getCode();
    switch (a) {
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
      case Q:
        MoveMotors(EnumCommandManual.Halt);
        break;
      case ESCAPE:
        this.enterNgz.setSelected(false);
        this.enterWaypoint.setSelected(false);
        eventBus.post(new EventUserMapNgzEnd());
        this.isFirstNgzPoint = true;
        this.infoNgzSelction.setVisible(false);
        break;
      default:
        Logger.debug("Key press:" + e.getCode() + " is not implemented");
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
      Scene help = new Scene(root);
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

  public void onClickZoomReset(MouseEvent mouseEvent) {
    eventBus.post(new EventUserZoomChanged(EnumZoomCommand.ZoomReset));
    mapDragInitial = new MapPoint(0,0);
    eventBus.post(new EventUserMapDragged(mapDragInitial.x,mapDragInitial.y));
  }

  public void onClickMap(MouseEvent mouseEvent) {
    if(mouseEvent.getButton() == MouseButton.SECONDARY) {
      mapDragInitial.x = mouseEvent.getX() - mapDragInitial.x;
      mapDragInitial.y = mouseEvent.getY() - mapDragInitial.y;
      Logger.debug("UI: map drag start...");
    }
    else if(enterNgz.isSelected()) {
      handleEnterNgz(mouseEvent);
    }
    else if(enterWaypoint.isSelected()) {
      Waypoint gotoOnMap = new Waypoint(mouseEvent.getX(), mouseEvent.getY());
      eventBus.post(new EventUserAddWaypoint(gotoOnMap));
      eventBus.post(new EventAutoControl(gotoOnMap));
    }
  }

  public void onClickEnterNgz(MouseEvent mouseEvent) {
    this.infoNgzSelction.setVisible(enterNgz.isSelected());
  }

  private void handleEnterNgz(MouseEvent mouseEvent) {
    if(isFirstNgzPoint) {
      this.eventBus.post(new EventUserMapNgzStart(mouseEvent.getX(), mouseEvent.getY()));
      isFirstNgzPoint = false;
    }
    else {
      this.eventBus.post(new EventUserMapNgzMiddle(mouseEvent.getX(), mouseEvent.getY()));
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
    Logger.debug("UI: map drag end...");
  }

  public void onClickMapImport(ActionEvent actionEvent) {
    Logger.log("Importing XML Map");
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Import XML Map");
    fileChooser.getExtensionFilters().addAll(
      new FileChooser.ExtensionFilter("xml", "*.xml")
    );
    Stage stage = (Stage) btnMoveStop.getScene().getWindow();
    File selectedFile = fileChooser.showOpenDialog(stage);
    if (selectedFile != null) {
      eventBus.post(new EventMapImport(selectedFile));
    }
  }

  public void onClickMapExport(ActionEvent actionEvent) {
    Logger.log("Exporting XML Map");
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Export XML Map");
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("xml", "*.xml")
    );
    Stage stage = (Stage) btnMoveStop.getScene().getWindow();
    File selectedFile = fileChooser.showSaveDialog(stage);
    if (selectedFile != null) {
      eventBus.post(new EventMapExport(selectedFile));
    }
  }

  public void onClickCloseUi(ActionEvent actionEvent) {
    Logger.log("Closing UI");
    Stage stage = (Stage) btnMoveStop.getScene().getWindow();
    stage.close();
  }
}
