package RobotRemote.Services.Workers.UiUpdater;

import RobotRemote.Models.MapPoint;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.Services.RobotWorkerBase;
import RobotRemote.UI.Views.RootController;
import com.google.common.eventbus.EventBus;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

  private void UpdateSensorsOnGUI() {
    Canvas layer = new Canvas(300,100);
    GraphicsContext gc = layer.getGraphicsContext2D();

    double sensorValUltra = appStateRepository.getSensorsState().getUltraReading();
    gc.setFill(Color.YELLOW);
    gc.fillRect(0,0,sensorValUltra + 80,40);

    double sensorValColourR = appStateRepository.getSensorsState().getColourReadingR()* 100;
    double sensorValColourG = appStateRepository.getSensorsState().getColourReadingG()* 100;
    double sensorValColourB = appStateRepository.getSensorsState().getColourReadingB()* 100;
    gc.setFill(Color.RED);
    gc.fillRect(0,40,sensorValColourR + 30,20);
    gc.setFill(Color.GREEN);
    gc.fillRect(0,60,sensorValColourG + 20,20);
    gc.setFill(Color.BLUE);
    gc.fillRect(0,80,sensorValColourB + 70,20);

    rootController.sensorDisplay.getChildren().clear();
    rootController.sensorDisplay.getChildren().add(layer);
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
