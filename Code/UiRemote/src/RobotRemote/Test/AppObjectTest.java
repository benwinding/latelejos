package RobotRemote.Test;

import RobotRemote.RobotServices.Connection.RobotConnectionState;
import RobotRemote.RobotServices.Movement.LocationState;
import RobotRemote.RobotServices.Movement.MovementState;
import RobotRemote.RobotServices.Sensors.DiscoveredColoursState;
import RobotRemote.RobotServices.Sensors.SensorsState;
import RobotRemote.Shared.AppStateRepository;
import RobotRemote.Shared.RobotConfiguration;
import RobotRemote.UI.UiState;
import RobotRemote.UIServices.MapHandlers.UserNoGoZoneState;
import RobotRemote.UIServices.MapHandlers.UserWaypointsState;
import RobotRemote.UIServices.UiUpdater.UiUpdaterState;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AppObjectTest {

    @Test
    public void SensorsObject(){
        RobotConfiguration robotConfiguration = new RobotConfiguration();
        AppStateRepository test=new AppStateRepository(robotConfiguration);
        assertNotNull(test.getSensorsState());
        assertTrue(String.valueOf(true), test.getSensorsState() instanceof SensorsState);
    }
    @Test
    public void LocationStateObject() {
        RobotConfiguration robotConfiguration = new RobotConfiguration();
        AppStateRepository test=new AppStateRepository(robotConfiguration);
        assertNotNull(test.getLocationState());
        assertTrue(String.valueOf(true), test.getLocationState() instanceof LocationState);
    }
    @Test
    public void getMovementStateObject() {
        RobotConfiguration robotConfiguration = new RobotConfiguration();
        AppStateRepository test=new AppStateRepository(robotConfiguration);
        assertNotNull(test.getMovementState());
        assertTrue(String.valueOf(true), test.getMovementState() instanceof MovementState);
    }
    @Test
    public void DiscoveredColoursStateObject() {
        RobotConfiguration robotConfiguration = new RobotConfiguration();
        AppStateRepository test=new AppStateRepository(robotConfiguration);
        assertNotNull(test.getDiscoveredColoursState());
        assertTrue(String.valueOf(true), test.getDiscoveredColoursState() instanceof DiscoveredColoursState);
    }
    @Test
    public void UserNoGoZoneStateObject() {
        RobotConfiguration robotConfiguration = new RobotConfiguration();
        AppStateRepository test=new AppStateRepository(robotConfiguration);
        assertNotNull(test.getUserNoGoZoneState());
        assertTrue(String.valueOf(true), test.getUserNoGoZoneState() instanceof UserNoGoZoneState);
    }
    @Test
    public void UiUpdaterStateObject() {
        RobotConfiguration robotConfiguration = new RobotConfiguration();
        AppStateRepository test=new AppStateRepository(robotConfiguration);
        assertNotNull(test.getUiUpdaterState());
        assertTrue(String.valueOf(true), test.getUiUpdaterState() instanceof UiUpdaterState);
    }
    @Test
    public void UiStateObject() {
        RobotConfiguration robotConfiguration = new RobotConfiguration();
        AppStateRepository test=new AppStateRepository(robotConfiguration);
        assertNotNull(test.getUiState());
        assertTrue(String.valueOf(true), test.getUiState() instanceof UiState);
    }
    @Test
    public void UserWaypointsStateObject() {
        RobotConfiguration robotConfiguration = new RobotConfiguration();
        AppStateRepository test=new AppStateRepository(robotConfiguration);
        assertNotNull(test.getUserWaypointsState());
        assertTrue(String.valueOf(true), test.getUserWaypointsState() instanceof UserWaypointsState);
    }
    @Test
    public void RobotConnectionStateObject() {
        RobotConfiguration robotConfiguration = new RobotConfiguration();
        AppStateRepository test=new AppStateRepository(robotConfiguration);
        assertNotNull(test.getRobotConnectionState());
        assertTrue(String.valueOf(true), test.getRobotConnectionState() instanceof RobotConnectionState);
    }

}
