package RobotRemote.Test;

import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.RobotServices.Movement.IMovementService;
import RobotRemote.RobotServices.Movement.MovementService;
import RobotRemote.RobotServices.Sensors.SensorsService;
import RobotRemote.RobotStateMachine.StateMachineBuilder;
import RobotRemote.Shared.*;
import RobotRemote.UI.Views.RootController;
import RobotRemote.UIServices.MapHandlers.MapInputEventHandlers;
import RobotRemote.UIServices.UiUpdater.UiUpdaterService;
import com.google.common.eventbus.EventBus;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static javafx.application.Application.launch;

public class UiConnectionTest {
    @Test
    public void Timeout() throws IOException {

    }
}
