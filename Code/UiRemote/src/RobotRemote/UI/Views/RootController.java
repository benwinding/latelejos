package RobotRemote.UI.Views;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.Enums.EnumCommandManual;
import RobotRemote.Models.Enums.EnumOperationMode;
import RobotRemote.Models.Enums.EnumZoomCommand;
import RobotRemote.Models.Events.*;
import RobotRemote.Models.MapPoint;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.UI.UiState;
import com.google.common.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
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
  public Pane statusSensorTouch;
  public Pane statusIsConnected;
  public Pane locationDetails;
  public Pane sensorDisplay;

  @FXML
  RadioButton enterNgz;
  @FXML
  RadioButton enterWaypoint;
  @FXML
  Label lblSwitchRobotMode;

  private UiState uiState;
  private EventBus eventBus;

  // Variables for UI logic
  private boolean isAutoMode;
  private MapPoint mapDragInitial = new MapPoint(0,0);

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Logger.log("UI Loaded!");
  }

  public void Init(RobotConfiguration config, EventBus eventBus, AppStateRepository appStateRepository) {
    this.uiState = appStateRepository.getUiState();
    this.eventBus = eventBus;
    this.isAutoMode = false;
    this.initMap();
    this.initSwitch();
  }

  private void initMap() {
    this.map.addEventHandler(ScrollEvent.SCROLL, event -> {
      double scrollAmount = event.getDeltaY();
      boolean isZoomIn = (scrollAmount > 0);
      eventBus.post(new EventUserZoomChanged(isZoomIn));
    });
  }

  private void initSwitch() {
    this.lblSwitchRobotMode.setText("Current Mode: Manual");
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
        Logger.log("Key press:" + e.getCode() + " is not implemented");
    }
  }

  public void onClickSwitchRobotMode(MouseEvent mouseEvent){
    if(isAutoMode) {
      isAutoMode = false;
      this.lblSwitchRobotMode.setText("Current Mode: Manual");
      eventBus.post(new EventChangeRobotCommand(EnumOperationMode.ManualMode));
    }
    else{
      isAutoMode = true;
      this.lblSwitchRobotMode.setText("Current Mode: Auto");
      eventBus.post(new EventChangeRobotCommand(EnumOperationMode.AutoMode));
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
    if(mouseEvent.getButton() == MouseButton.SECONDARY) {
      mapDragInitial.x = mouseEvent.getX();
      mapDragInitial.y = mouseEvent.getY();
      Logger.log("UI: Map begin dragged...");
    }
    else if(enterNgz.isSelected()) {
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
    mapDragInitial = new MapPoint(0,0);
    eventBus.post(new EventUserMapDragged(mapDragInitial.x,mapDragInitial.y));
  }

  public void onDragMap(MouseEvent dragEvent) {
    if(dragEvent.getButton() != MouseButton.SECONDARY)
      return;

    double xi = mapDragInitial.x;
    double yi = mapDragInitial.y;
    MapPoint dragDelta = new MapPoint(
        dragEvent.getX() - mapDragInitial.x,
        dragEvent.getY() - mapDragInitial.y
    );
    double x = dragEvent.getX();
    double y = dragEvent.getY();
//    Logger.log(String.format("dragged x:%.1f, y:%.1f",dragDelta.x,dragDelta.x));
//    Logger.log(String.format("dragged x:%.1f, y:%.1f",mapDragInitial.x,mapDragInitial.x));
    eventBus.post(new EventUserMapDragged(dragDelta.x,dragDelta.y));
  }

  public void onReleaseMap(MouseEvent mouseEvent) {
    MapPoint dragNew = new MapPoint(
        mouseEvent.getX() - mapDragInitial.x,
        mouseEvent.getY() - mapDragInitial.y
    );
    this.mapDragInitial = dragNew;
    Logger.log("UI: Map end dragged...");
  }
}
