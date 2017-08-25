package RobotRemote;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManualView.fxml"));
        Parent root = (Parent) loader.load();

        // Setup controller
        ManualController manualController = loader.getController();
        Scene scene = new Scene(root, 800, 600);
        // Init things
        float initX = 100;
        float initY = 100;
        manualController.Init(scene, initX, initY);
        Logger.Init(scene);
        RobotMotorManager.InitMotors(initX, initY);

        primaryStage.setTitle("Robot Remote UI");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    @Override
    public void stop(){
        System.out.println("Stage is closing");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
