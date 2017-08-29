package RobotRemote;

import RobotRemote.Services.RobotMoveService;
import RobotRemote.Views.Main.ManualController;
import RobotRemote.Services.Mocks.TestingMotorManager;
import RobotRemote.Utils.Logger;
import RobotRemote.Utils.ThreadManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/RobotRemote/Views/Main/ManualView.fxml"));
        Parent root = (Parent) loader.load();

        // Setup controller
        ManualController manualController = loader.getController();
        Scene scene = new Scene(root, 1000, 700);

        // Init things
        Logger.Init(scene);
        float initX = 100;
        float initY = 100;
        float initTheta = 0;
        manualController.Init(initX, initY, initTheta);
        RobotMoveService.InitMotors(initX, initY, initTheta);
        TestingMotorManager.InitMotors(initX, initY, initTheta);

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
