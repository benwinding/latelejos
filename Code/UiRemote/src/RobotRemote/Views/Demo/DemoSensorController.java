package RobotRemote.Views.Demo;

import RobotRemote.Services.RobotSensorService;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class DemoSensorController implements Initializable {
    private static RobotSensorService sensor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sensor=new RobotSensorService();
    }

    public void onClickSensor(){
        sensor.init();
        sensor.start();
    }

    public void onClickSensorStop(){
        sensor.terminate();
    }
}
