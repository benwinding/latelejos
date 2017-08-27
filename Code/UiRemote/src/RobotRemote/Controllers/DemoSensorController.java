package RobotRemote.Controllers;

import RobotRemote.RobotSensorManager;
import RobotRemote.Utils.Logger;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class DemoSensorController implements Initializable {
    private static RobotSensorManager sensor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sensor=new RobotSensorManager();
    }

    public void onClickSensor(){
        sensor.init();
        sensor.start();
    }

    public void onClickSensorStop(){
        sensor.terminate();
    }
}
