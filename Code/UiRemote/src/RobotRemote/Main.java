package RobotRemote;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("remoteUi.fxml"));
        primaryStage.setTitle("Robot Remote UI");
        primaryStage.setScene(new Scene(root, 700, 400));

        MotorCommander.InitMotors();

        primaryStage.show();
    }

    @Override
    public void stop(){
        System.out.println("Stage is closing");
        MotorCommander.ShutdownMotors();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
