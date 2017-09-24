package RobotRemote;

import RobotRemote.RobotServices.Movement.MovementService;
import RobotRemote.Shared.Logger;
import RobotRemote.Shared.RobotConfiguration;
import RobotRemote.Shared.AppStateRepository;
import RobotRemote.RobotStateMachine.StateMachineBuilder;
import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.Shared.ServiceManager;
import RobotRemote.Shared.ThreadLoop;
import RobotRemote.UIServices.MapHandlers.Lunarovermap;
import RobotRemote.UIServices.MapHandlers.MapInputEventHandlers;
import RobotRemote.RobotServices.Movement.IMovementService;
import RobotRemote.RobotServices.Sensors.SensorsService;
import RobotRemote.UIServices.MapHandlers.RobotMapTranslator;
import RobotRemote.UIServices.UiUpdater.UiUpdaterService;
import RobotRemote.UI.Views.RootController;
import com.google.common.eventbus.EventBus;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.util.ArrayList;

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
    serviceManager.StartAllThreads();

    // Handler classes
    MapInputEventHandlers userInputEventHandlers = new MapInputEventHandlers(serviceManager);

      // State Machine Builder
    StateMachineBuilder stateMachineBuilder = new StateMachineBuilder(serviceManager);

    // Initialize the UI controller
    rootController.Init(serviceManager);

    // Show GUI
    primaryStage.setTitle("Robot Remote UI");
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true);
    primaryStage.show();

//test map xml stuff

      Lunarovermap map1 = new Lunarovermap();
      RobotMapTranslator translator = new RobotMapTranslator();
      map1=translator.createMapObject("UiRemote/src/RobotRemote/UIServices/MapHandlers/samplexml.xml");
      System.out.println("Rover Landing point : " + map1.roverLandingSite.getPoint().getX() + " , " + map1.roverLandingSite.getPoint().getY());
      map1.roverLandingSite.point.setX(100);
      map1.roverLandingSite.point.setY(100);
      System.out.println("New Rover Landing point : " + map1.roverLandingSite.getPoint().getX() + " , " + map1.roverLandingSite.getPoint().getY());
      translator.createXml(map1);
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
