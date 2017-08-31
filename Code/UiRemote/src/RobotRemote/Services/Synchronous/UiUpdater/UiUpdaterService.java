package RobotRemote.Services.Synchronous.UiUpdater;

import RobotRemote.Models.MapPoint;
import RobotRemote.Repositories.RobotRepository;
import RobotRemote.Services.Asynchronous.Movement.LocationState;
import RobotRemote.Services.RobotServiceBase;
import RobotRemote.UI.Views.RootController;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class UiUpdaterService extends RobotServiceBase {
  private RobotRepository robotRepository;
  private RootController rootController;

  public UiUpdaterService(RobotRepository robotRepository, RootController rootController) {
    super("GUI Updater Service", 300);
    this.robotRepository = robotRepository;
    this.rootController = rootController;
  }

  @Override
  protected void OnStart() {

  }

  @Override
  public void Repeat() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        UpdateGuiThreadSafe();
      }
    });
  }

  private void UpdateGuiThreadSafe() {
    UpdateSensorsOnGUI();
    UpdateMapOnGUI();
    UpdateLocationOnGUI();
  }

  private void UpdateLocationOnGUI() {
    VBox locationPane = rootController.locationDetails;
    locationPane.getChildren().clear();

    MapPoint pos = robotRepository.getLocationState().GetCurrentPosition();
    AddTextToPane(locationPane, String.format("x: %f, y: %f, rot: %f", pos.x, pos.y, pos.theta));
    AddTextToPane(locationPane, String.format("Motors are: %s", robotRepository.getMovementState().getMotorState()));
    AddTextToPane(locationPane, String.format("Ui Command: %s", robotRepository.getUiState().getCurrentCommand()));
  }

  private void AddTextToPane(Pane pane, String format) {
    Text newText = new Text();
    newText.setText(format);
    pane.getChildren().add(newText);
  }

  private void UpdateSensorsOnGUI() {
    double val = robotRepository.getSensorsState().getColourReadingR();
    //rootController.messageDisplayer.appendText("Current Val: " + val + "\n");
  }

  private void UpdateMapOnGUI() {
    rootController.map.getChildren().clear();
    UiUpdaterState uiUpdaterState = robotRepository.getUiUpdaterState();
    LocationState locationState = robotRepository.getLocationState();

    MapLayerFactory mapFactory = new MapLayerFactory(uiUpdaterState, locationState);
    List<Canvas> allMapLayers = mapFactory.CreateMapLayers();
    // Add to GUI
    rootController.map.getChildren().clear();
    rootController.map.getChildren().addAll(allMapLayers);
  }

  @Override
  protected void OnShutdown() {}
}
