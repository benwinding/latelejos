package RobotRemote;

import RobotRemote.Helpers.Logger;
import RobotRemote.Helpers.ThreadManager;
import RobotRemote.Models.RobotConfig;
import RobotRemote.Repositories.RobotRepository;
import RobotRemote.Services.*;
import RobotRemote.Services.Asynchronous.Movement.RobotMoveService;
import RobotRemote.Services.Mocks.TestingMoveService;
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

    // Spin up threads
    RobotRepository rr = new RobotRepository(robotConfig);
    ServiceLocator sl = new ServiceLocator(rr, rootController);
    serviceUmpire = new ServiceUmpire(sl);

    // Init things
    Logger.Init(scene);
    rootController.Init(robotConfig);
    RobotMoveService.InitMotors(robotConfig);
    TestingMoveService.InitMotors(robotConfig);

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
