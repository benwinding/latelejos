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
        Scene scene = new Scene(root, 700, 400);
        manualController.setScene(scene);

        primaryStage.setTitle("Robot Remote UI");
        primaryStage.setScene(scene);

        RobotMotorManager.InitMotors();

        primaryStage.show();
    }

    @Override
    public void stop(){
        System.out.println("Stage is closing");
        RobotMotorManager.ShutdownMotors();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
