package RobotRemote;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.Services.Listeners.Connection.RobotConnectionService;
import RobotRemote.Services.Listeners.Movement.MovementEventListener;
import RobotRemote.Services.Listeners.Movement.PilotFactory;
import RobotRemote.Services.Listeners.StateMachine.StateMachineListener;
import RobotRemote.Services.ServiceLocator;
import RobotRemote.Services.ServiceUmpire;
import RobotRemote.Services.Workers.SensorService.SensorsService;
import RobotRemote.Services.Workers.UiUpdater.UiUpdaterService;
import RobotRemote.UI.Views.RootController;
import com.google.common.eventbus.EventBus;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lejos.robotics.navigation.ArcRotateMoveController;

public class Main extends Application {

  private ServiceUmpire serviceUmpire;

  @Override
  public void start(Stage primaryStage) throws Exception{
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/RobotRemote/UI/Views/RootView.fxml"));
    Parent root = (Parent) loader.load();

    // Setup controller
    RootController rootController = loader.getController();
    Scene scene = new Scene(root, 1000, 700);
    scene.getStylesheets().add("/RobotRemote/UI/Views/RootStyle.css");
    Logger.Init(scene);

    // Get Application Configuration
    RobotConfiguration robotConfiguration = new RobotConfiguration();
    // Instantiate repository
    AppStateRepository appStateRepository = new AppStateRepository(robotConfiguration);
    // robotConnection Service
    RobotConnectionService robotConnectionService = new RobotConnectionService();

    // Instantiate EventBus
    EventBus eventBus = new EventBus();


    // Worker threads
    SensorsService sensorService = new SensorsService(robotConfiguration, robotConnectionService, appStateRepository.getSensorsState());
    UiUpdaterService uiUpdaterService = new UiUpdaterService(robotConfiguration, appStateRepository, rootController, eventBus);

    // Instantiate movement listener
    ArcRotateMoveController pilot = PilotFactory.GetPilot(robotConnectionService, robotConfiguration);
    MovementEventListener movementListener = new MovementEventListener(robotConfiguration, pilot, appStateRepository, eventBus, sensorService);

    // Instantiate state machine listener
    StateMachineListener stateMachineListener = new StateMachineListener(
        appStateRepository,
        eventBus
    );

    // Instantiate service locator
    ServiceLocator serviceLocator = new ServiceLocator(
        robotConnectionService,
        sensorService,
        uiUpdaterService,
        movementListener
    );
    // Spin up threads
    serviceUmpire = new ServiceUmpire(serviceLocator);
    serviceUmpire.StartAllThreads();

    rootController.Init(robotConfiguration, appStateRepository.getUiState(), eventBus, robotConnectionService);

    // Show GUI
    primaryStage.setTitle("Robot Remote UI");
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }

  @Override
  public void stop(){
    System.out.println("Stage is closing");
    serviceUmpire.StopAllThreads();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
