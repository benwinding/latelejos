package RobotRemote;

import RobotRemote.Shared.Logger;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Shared.AppStateRepository;
import RobotRemote.RobotStateMachine.StateMachineBuilder;
import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.Shared.ServiceManager;
import RobotRemote.UIServices.MapHandlers.MapInputEventHandlers;
import RobotRemote.RobotServices.Movement.MoveThread;
import RobotRemote.RobotServices.Sensors.SensorsService;
import RobotRemote.UIServices.ServiceLocator;
import RobotRemote.UIServices.UiUpdater.UiUpdaterService;
import RobotRemote.UI.Views.RootController;
import com.google.common.eventbus.EventBus;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  private ServiceManager serviceManager;
  public static AppStateRepository appStateRepository;
    @Override
  public void start(Stage primaryStage) throws Exception{
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/RobotRemote/UI/Views/RootView.fxml"));
    Parent root = (Parent) loader.load();

    // Setup root view controller
    RootController rootController = loader.getController();
    Scene scene = new Scene(root, 1000, 700);
    scene.getStylesheets().add("/RobotRemote/UI/Views/RootStyle.css");

    // Link logger tot the root view, messageDisplay component
    Logger.Init(scene);

    // Get Application Configuration
    RobotConfiguration robotConfiguration = new RobotConfiguration();

    // Instantiate all app state
    appStateRepository = new AppStateRepository(robotConfiguration);

    // Connection to the robot
    RobotConnectionService robotConnectionService = new RobotConnectionService(appStateRepository);

    // Instantiate EventBus
    EventBus eventBus = new EventBus();

    // Daemons
    SensorsService sensorService = new SensorsService(robotConfiguration, robotConnectionService, appStateRepository);
    UiUpdaterService uiUpdaterService = new UiUpdaterService(robotConfiguration, appStateRepository, rootController);

    // Handler classes
    MapInputEventHandlers userInputEventHandlers = new MapInputEventHandlers(eventBus, robotConfiguration, appStateRepository);

    MoveThread moveThread = new MoveThread();
    // Coordinate and spin up services
    serviceManager = new ServiceManager(
        robotConfiguration,
        appStateRepository,
        robotConnectionService,
        sensorService,
        uiUpdaterService,
        moveThread
    );
    serviceManager.StartAllThreads();

    // Service locator
    ServiceLocator serviceLocator = new ServiceLocator(
        moveThread
    );

    // State Machine Builder
    StateMachineBuilder stateMachineBuilder = new StateMachineBuilder(eventBus, serviceLocator, appStateRepository);

    // Initialize the UI controller
    rootController.Init(
        robotConfiguration,
        eventBus,
        appStateRepository
    );

    // Show GUI
    primaryStage.setTitle("Robot Remote UI");
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }

  @Override
  public void stop(){
    System.out.println("Stage is closing");
    serviceManager.StopAllThreads();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
