package RobotRemote;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.MapPoint;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.Services.Connection.RobotConnectionService;
import RobotRemote.Services.MapHandlers.MapInputEventHandlers;
import RobotRemote.Services.MapHandlers.MapTransferObject;
import RobotRemote.Services.MapHandlers.RobotMapTranslator;
import RobotRemote.Services.Movement.MoveThreads.LocationState;
import RobotRemote.Services.Movement.MovementHandler;
import RobotRemote.Services.RobotCommander.RobotCommandListener;
import RobotRemote.Services.Sensors.SensorsService;
import RobotRemote.Services.ServiceCoordinator;
import RobotRemote.Services.UiUpdater.UiUpdaterService;
import RobotRemote.UI.Views.RootController;
import com.google.common.eventbus.EventBus;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

  private ServiceCoordinator serviceCoordinator;
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
    UiUpdaterService uiUpdaterService = new UiUpdaterService(eventBus, robotConfiguration, appStateRepository, rootController);

    // Handler classes
    MovementHandler movementHandler = new MovementHandler(eventBus, robotConfiguration, appStateRepository, robotConnectionService);
    MapInputEventHandlers userInputEventHandlers = new MapInputEventHandlers(eventBus, robotConfiguration, appStateRepository);

    // Instantiate robot commander
    RobotCommandListener robotCommanderService = new RobotCommandListener(
        appStateRepository,
        eventBus
    );

    // Coordinate and spin up services
    serviceCoordinator = new ServiceCoordinator(
        robotConnectionService,
        sensorService,
        uiUpdaterService,
        movementHandler
    );
    serviceCoordinator.StartAllThreads();

    // Initialize the
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


    MapPoint currentPosition=new MapPoint(0,0,0);
    ArrayList<MapPoint> noGoZones=new ArrayList<>();
    ArrayList<MapPoint> tracks=new ArrayList<>();
    ArrayList<MapPoint> radiation=new ArrayList<>();
    ArrayList<MapPoint> craters=new ArrayList<>();
    MapTransferObject map1 = new MapTransferObject(currentPosition,noGoZones,tracks,radiation, craters);
    RobotMapTranslator translator = new RobotMapTranslator();
    translator.createXml(map1);
  }

  @Override
  public void stop(){
    System.out.println("Stage is closing");
    serviceCoordinator.StopAllThreads();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
