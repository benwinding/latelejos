package RobotRemote;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.RobotConfig;
import RobotRemote.Repositories.RobotRepository;
import RobotRemote.Services.Asynchronous.Movement.MovementEventListener;
import RobotRemote.Services.Mocks.TestArcPilot;
import RobotRemote.Services.ServiceLocator;
import RobotRemote.Services.ServiceUmpire;
import RobotRemote.Services.Synchronous.Connection.RobotConnectionService;
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
    Logger.Init(scene);

    // Get Application Configuration
    RobotConfig robotConfig = new RobotConfig();
    // Instantiate repository
    RobotRepository robotRepository = new RobotRepository(robotConfig);
    // robotConnection Service
    RobotConnectionService robotConnectionService = new RobotConnectionService();

    // Instantiate EventBus
    EventBus eventBus = new EventBus();

    // Instantiate movement service
    ArcRotateMoveController pilot = GetPilot(robotConnectionService, robotConfig);
    MovementEventListener movementService = new MovementEventListener(robotConfig, pilot, robotRepository, eventBus);

    // Instantiate service locator
    ServiceLocator serviceLocator = new ServiceLocator(
        robotConnectionService,
        robotRepository,
        rootController,
        movementService
    );
    // Spin up threads
    serviceUmpire = new ServiceUmpire(serviceLocator);
    serviceUmpire.StartAllThreads();

    rootController.Init(robotConfig, robotRepository.getUiState(), eventBus);

    // Show GUI
    primaryStage.setTitle("Robot Remote UI");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private ArcRotateMoveController GetPilot(RobotConnectionService connectionService, RobotConfig config) {
    connectionService.InitializeBrick();
    ArcRotateMoveController pilot;
    if(connectionService.IsConnected()) {
      pilot = connectionService.GetBrick().createPilot(
          config.robotWheelDia,
          config.robotTrackWidth,
          config.wheelPortLeft,
          config.wheelPortRight
      );
      Logger.Log("Robot is connected, using robots pilot");
    }
    else {
      Logger.Log("Robot not connected, using TestArcPilot");
      pilot = new TestArcPilot();
    }
    pilot.setLinearSpeed(config.robotLinearSpeed_cms);
    pilot.setLinearAcceleration(config.robotLinearAcceleration_cms2);
    pilot.setAngularSpeed(config.robotAngularSpeed_degs);
    pilot.setAngularAcceleration(config.robotAngularAcceleration_degs2);
    return pilot;
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
