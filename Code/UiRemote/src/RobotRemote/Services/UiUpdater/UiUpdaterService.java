package RobotRemote.Services.UiUpdater;

import RobotRemote.Models.MapPoint;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.Services.RobotServiceBase;
import RobotRemote.Services.Sensors.SensorsState;
import RobotRemote.UI.Views.RootController;
import com.google.common.eventbus.EventBus;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class UiUpdaterService extends RobotServiceBase {
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
    UpdateMapOnGUI();
    UpdateLocationOnGUI();
    UpdateSensorsOnGUI(appStateRepository.getSensorsState());
  }

  private void UpdateLocationOnGUI() {
    Pane vbox = new VBox();
    MapPoint pos = appStateRepository.getLocationState().GetCurrentPosition();
    AddTextToPane(vbox, String.format("x: %d, y: %d, rot: %d", Math.round(pos.x), Math.round(pos.y), Math.round(pos.theta)));
    AddTextToPane(vbox, String.format("Motors are: %s", appStateRepository.getMovementState().getMotorState()));
    AddTextToPane(vbox, String.format("Ui Command: %s", appStateRepository.getUiState().getCurrentCommand()));
    Pane locationPanes = rootController.locationDetails;
    locationPanes.getChildren().clear();
    locationPanes.getChildren().add(vbox);
  }

  private void AddTextToPane(Pane pane, String format) {
    Text newText = new Text();
    newText.setText(format);
    pane.getChildren().add(newText);
  }

  private void UpdateSensorsOnGUI(SensorsState sensorsState) {
    Node sensorsGraph = SensorsDisplayLayerFactory.CreateSensorsGraph(sensorsState);
    rootController.sensorDisplay.getChildren().clear();
    rootController.sensorDisplay.getChildren().add(sensorsGraph);
  }

  private void UpdateMapOnGUI() {
    MapLocationsLayersFactory mapLocationLayersFactory = new MapLocationsLayersFactory(appStateRepository);
    List<Canvas> mapLocationLayers = mapLocationLayersFactory.CreateMapLayers();

    MapSelectedLayersFactory mapSelectedLayersFactory = new MapSelectedLayersFactory(appStateRepository);
    List<Canvas> mapSelectedLayers = mapSelectedLayersFactory.CreateMapLayers();

    rootController.map.getChildren().clear();
    rootController.map.getChildren().addAll(mapLocationLayers);
    rootController.map.getChildren().addAll(mapSelectedLayers);
  }
}
