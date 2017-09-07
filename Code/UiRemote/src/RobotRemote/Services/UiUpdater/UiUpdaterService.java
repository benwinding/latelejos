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
    super("UI Updater Service", robotConfiguration.updateIntervalUi_ms);
    this.appStateRepository = appStateRepository;
    this.rootController = rootController;
    this.eventBus = eventBus;
  }

  @Override
  public void Repeat() {
    Platform.runLater(() -> UpdateUiThreadSafe());
  }

  private void UpdateUiThreadSafe() {
    UpdateMapOnUi();
    UpdateLocationOnUi();
    UpdateSensorsOnUi();
    UpdateStatusDisplay();
  }

  private void UpdateLocationOnUi() {
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

  private void UpdateSensorsOnUi() {
    Node sensorsGraph = SensorsDisplayLayerFactory.CreateSensorsGraph(appStateRepository.getSensorsState());
    rootController.sensorDisplay.getChildren().clear();
    rootController.sensorDisplay.getChildren().add(sensorsGraph);
  }

  private void UpdateMapOnUi() {
    MapLocationsLayersFactory mapLocationLayersFactory = new MapLocationsLayersFactory(appStateRepository);
    List<Canvas> mapLocationLayers = mapLocationLayersFactory.CreateMapLayers();

    MapSelectedLayersFactory mapSelectedLayersFactory = new MapSelectedLayersFactory(appStateRepository);
    List<Canvas> mapSelectedLayers = mapSelectedLayersFactory.CreateMapLayers();

    rootController.map.getChildren().clear();
    rootController.map.getChildren().addAll(mapLocationLayers);
    rootController.map.getChildren().addAll(mapSelectedLayers);
  }

  private void UpdateStatusDisplay() {
    SensorsState sensorsState = appStateRepository.getSensorsState();
    UpdateStatusOnElement(rootController.statusSensorUltra, sensorsState.getStatusUltra());
    UpdateStatusOnElement(rootController.statusSensorColour, sensorsState.getStatusColour());
    UpdateStatusOnElement(rootController.statusSensorTouch, sensorsState.getStatusTouch());
    UpdateStatusOnElement(rootController.statusIsConnected, appStateRepository.getRobotConnectionState().isConnected());
  }

  private void UpdateStatusOnElement(Pane element, boolean status) {
    Canvas statusSensorUltra = StatusDisplayFactory.CreateStatusGreen(status, 15);
    element.getChildren().clear();
    element.getChildren().addAll(statusSensorUltra);
  }
}
