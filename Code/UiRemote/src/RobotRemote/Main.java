package RobotRemote;

import RobotRemote.Helpers.Logger;
import RobotRemote.Helpers.ThreadManager;
import RobotRemote.Models.RobotConfig;
import RobotRemote.Services.Asynchronous.Movement.RobotMoveService;
import RobotRemote.Services.Mocks.TestingMoveService;
import RobotRemote.UI.Views.Main.ManualController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception{
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/RobotRemote/UI/Views/Main/ManualView.fxml"));
    Parent root = (Parent) loader.load();

    // Setup controller
    ManualController manualController = loader.getController();
    Scene scene = new Scene(root, 1000, 700);

    RobotConfig robotConfig = new RobotConfig().GetConfiguration("/robot.config.txt");

    // Init things
    Logger.Init(scene);
    manualController.Init(robotConfig);
    RobotMoveService.InitMotors(robotConfig);
    TestingMoveService.InitMotors(robotConfig);

    primaryStage.setTitle("Robot Remote UI");
    primaryStage.setScene(scene);

    primaryStage.show();
  }
  @Override
  public void stop(){
    System.out.println("Stage is closing");
    ThreadManager.KillAll();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
