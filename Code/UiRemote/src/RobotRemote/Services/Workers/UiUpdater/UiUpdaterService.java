package RobotRemote.Services.Workers.UiUpdater;

import RobotRemote.Models.MapPoint;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.Services.RobotWorkerBase;
import RobotRemote.UI.Views.RootController;
import com.google.common.eventbus.EventBus;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class UiUpdaterService extends RobotWorkerBase {
  private AppStateRepository appStateRepository;
  private RootController rootController;
  private EventBus eventBus;

  public UiUpdaterService(RobotConfiguration robotConfiguration, AppStateRepository appStateRepository, RootController rootController, EventBus eventBus) {
    super("GUI Updater Service", robotConfiguration.updateIntervalUi_ms);
    this.appStateRepository = appStateRepository;
    this.rootController = rootController;
    this.eventBus = eventBus;
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

    MapPoint pos = appStateRepository.getLocationState().GetCurrentPosition();
    AddTextToPane(locationPane, String.format("x: %d, y: %d, rot: %d", Math.round(pos.x), Math.round(pos.y), Math.round(pos.theta)));
    AddTextToPane(locationPane, String.format("Motors are: %s", appStateRepository.getMovementState().getMotorState()));
    AddTextToPane(locationPane, String.format("Ui Command: %s", appStateRepository.getUiState().getCurrentCommand()));
  }

  private void AddTextToPane(Pane pane, String format) {
    Text newText = new Text();
    newText.setText(format);
    pane.getChildren().add(newText);
  }

  private void UpdateSensorsOnGUI() {
    double val = appStateRepository.getSensorsState().getColourReadingR();
    //rootController.messageDisplayer.appendText("Current Val: " + val + "\n");
  }

  private void UpdateMapOnGUI() {
    rootController.map.getChildren().clear();
    MapLayerFactory mapFactory =
        new MapLayerFactory(
        eventBus,
        appStateRepository.getUiUpdaterState(),
        appStateRepository.getLocationState(),
        appStateRepository.getUserNoGoZoneState());
    List<Canvas> allMapLayers = mapFactory.CreateMapLayers();
    // Add to GUI
    rootController.map.getChildren().clear();
    rootController.map.getChildren().addAll(allMapLayers);
  }
}
