package RobotRemote;

import RobotRemote.Services.RobotMoveService;
import RobotRemote.Views.Main.ManualController;
import RobotRemote.Services.Mocks.TestingMoveService;
import RobotRemote.Utils.Logger;
import RobotRemote.Utils.ThreadManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.net.URL;
import java.util.NoSuchElementException;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception{
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/RobotRemote/Views/Main/ManualView.fxml"));
    Parent root = (Parent) loader.load();

    // Setup controller
    ManualController manualController = loader.getController();
    Scene scene = new Scene(root, 1000, 700);

    float initX = 100;
    float initY = 100;
    float initTheta = 0;
    Configurations configs = new Configurations();
    try
    {
      URL file = getClass().getResource("/robot.config.txt");
      Configuration config = configs.properties(file);
      // access configuration properties
      initX = config.getFloat("location.x");
      initY = config.getFloat("location.y");
      initTheta = config.getFloat("location.heading");
      Logger.Log("Configuration Successfully Read");
    }
    catch (ConfigurationException cex)
    {
      Logger.Log("Some problem with the configuration");
    }
    catch (NoSuchElementException ex) {
      Logger.Log("Configuration Error: " + ex.getLocalizedMessage());
      return;
    }

    // Init things
    Logger.Init(scene);
    manualController.Init(initX, initY, initTheta);
    RobotMoveService.InitMotors(initX, initY, initTheta);
    TestingMoveService.InitMotors(initX, initY, initTheta);

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
