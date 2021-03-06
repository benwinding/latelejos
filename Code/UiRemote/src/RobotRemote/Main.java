package RobotRemote;

import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.RobotServices.Movement.IMovementService;
import RobotRemote.RobotServices.Movement.Mocks.MockSensor;
import RobotRemote.RobotServices.Movement.MovementService;
import RobotRemote.RobotServices.Sensors.SensorsService;
import RobotRemote.RobotStateMachine.StateMachineBuilder;
import RobotRemote.Shared.*;
import RobotRemote.UI.Views.RootController;
import RobotRemote.UIServices.MapHandlers.MapExportHandlers;
import RobotRemote.UIServices.MapHandlers.MapInputEventHandlers;
import RobotRemote.UIServices.UiUpdater.UiUpdaterService;
import com.google.common.eventbus.EventBus;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
  private ServiceManager serviceManager;

  @Override
  public void start(Stage primaryStage) throws Exception{
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/RobotRemote/UI/Views/RootView.fxml"));
    Parent root = (Parent) loader.load();

    // Setup root view controller
    RootController rootController = loader.getController();
    Scene scene = new Scene(root, 1000, 700);

    // Link logger tot the root view, messageDisplay component
    Logger.Init(scene);

    // Get Application Configuration
    RobotConfiguration robotConfiguration = new RobotConfiguration();

    // Instantiate all app state
    AppStateRepository appStateRepository = new AppStateRepository(robotConfiguration);

    // Connection to the robot
    RobotConnectionService robotConnectionService = new RobotConnectionService(appStateRepository);

    // Instantiate EventBus
    EventBus eventBus = new EventBus();

    // Daemons
    SensorsService sensorService = new SensorsService(robotConfiguration, robotConnectionService, appStateRepository, eventBus);
    UiUpdaterService uiUpdaterService = new UiUpdaterService(robotConfiguration, appStateRepository, rootController);

    IMovementService movementService = new MovementService();
    ThreadLoop stateMachineThreadLoop = new ThreadLoop("Robot State Machine");

    // Coordinate and spin up services
    serviceManager = new ServiceManager(
        eventBus,
        robotConfiguration,
        appStateRepository,
        robotConnectionService,
        sensorService,
        uiUpdaterService,
        movementService,
        stateMachineThreadLoop
    );
    serviceManager.startAllThreads();

    // Handler classes
    MapInputEventHandlers userInputEventHandlers = new MapInputEventHandlers(serviceManager);
    MapExportHandlers mapExportHandlers = new MapExportHandlers(serviceManager);

      // State Machine Builder
    StateMachineBuilder stateMachineBuilder = new StateMachineBuilder(serviceManager);

    // Initialize the UI controller
    rootController.Init(serviceManager);

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
