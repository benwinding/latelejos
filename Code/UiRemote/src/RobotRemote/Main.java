package RobotRemote;

import RobotRemote.Helpers.Logger;
import RobotRemote.Helpers.ThreadManager;
import RobotRemote.Models.RobotConfig;
import RobotRemote.Repositories.RobotRepository;
import RobotRemote.Services.Asynchronous.Movement.RobotMoveService;
import RobotRemote.Services.Mocks.TestingMoveService;
import RobotRemote.Services.ServiceLocator;
import RobotRemote.Services.ServiceUmpire;
import RobotRemote.Services.Synchronous.Connection.RobotConnectionService;
import RobotRemote.UI.Views.RootController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  private ServiceUmpire serviceUmpire;

  @Override
  public void start(Stage primaryStage) throws Exception{
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/RobotRemote/UI/Views/RootView.fxml"));
    Parent root = (Parent) loader.load();

    // Setup controller
    RootController rootController = loader.getController();
    Scene scene = new Scene(root, 1000, 700);

    // Get Application Configuration
    RobotConfig robotConfig = new RobotConfig().GetConfiguration("/robot.config.txt");

    // Prepare main controller
    Logger.Init(scene);
    RobotConnectionService robotConnectionService = new RobotConnectionService(robotConfig);
    RobotMoveService rbs = new RobotMoveService(robotConfig, robotConnectionService);
    TestingMoveService tms = new TestingMoveService(robotConfig, robotConnectionService);
    rootController.Init(robotConfig, rbs, tms);

    // Spin up threads
    RobotRepository robotRepository = new RobotRepository(robotConfig);
    ServiceLocator serviceLocator = new ServiceLocator(robotConnectionService,robotRepository,rootController);
    serviceUmpire = new ServiceUmpire(serviceLocator);
    serviceUmpire.StartAllThreads();

    primaryStage.setTitle("Robot Remote UI");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Override
  public void stop(){
    System.out.println("Stage is closing");
    ThreadManager.KillAll();
    serviceUmpire.StopAllThreads();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
