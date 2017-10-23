package RobotRemote.RobotStateMachine.States.AutoMode;

import RobotRemote.RobotServices.Movement.LocationState;
import RobotRemote.RobotServices.Sensors.SensorsState;
import RobotRemote.RobotStateMachine.Events.AutoSurvey.EventAutomapDetectedObject;
import RobotRemote.Shared.RobotConfiguration;
import RobotRemote.Shared.ServiceManager;
import RobotRemote.UIServices.MapHandlers.NgzUtils;
import com.google.common.eventbus.EventBus;
import javafx.scene.paint.Color;
import lejos.robotics.navigation.Pose;

import java.awt.*;

public class AutoSurveyUtil
{
  private ServiceManager sm;
  private SensorsState sensorState;
  private RobotConfiguration config;
  private EventBus eventBus;
  private LocationState locationState;

  public AutoSurveyUtil(ServiceManager sm)
  {
    this.sm = sm;
    this.locationState = this.sm.getAppState().getLocationState();
    this.eventBus = sm.getEventBus();
    this.sensorState = this.sm.getAppState().getSensorsState();
    this.config = this.sm.getConfiguration();
  }

  public boolean isThereAnObject()
  {
    if (!sm.getAppState().getSensorsState().getStatusUltra())
      return false;
    return sensorState.getUltraReadingCm() < config.obstacleAvoidDistance;
  }

  public boolean isThereANGZ()
  {
    Rectangle reg = NgzUtils.getInterceptNgzArea(sm.getAppState(), config);
    if (reg != null)
    {
      //Check if forward collide
      Pose currentPose = locationState.GetCurrentPose();
      float currentDistance = currentPose.distanceTo(new lejos.robotics.geometry.Point(reg.x, reg.y));
      currentPose.moveUpdate(3);
      float forwardDistance = currentPose.distanceTo(new lejos.robotics.geometry.Point(reg.x, reg.y));
      return currentDistance > forwardDistance;
    }
    return false;

  }

  public boolean isThereABorder()
  {
    javafx.scene.paint.Color color = sensorState.getColourEnum();
    return color == config.colorBorder;
  }

  public boolean isThereACrater()
  {
    return sensorState.getColourEnum() == config.colorCrater;
  }

  public boolean isThereApollo()
  {
    return sensorState.getColourEnum() == config.colorApollo;
  }

  public boolean isThereApolloAsObject(AutoSurveying.AutoSurveyingInternalState state)
  {
    return isThereAnObject() && state == AutoSurveying.AutoSurveyingInternalState.SurveyRadiation;
  }

  public boolean isThereATrail()
  {
    return sensorState.getColourEnum() == config.colorTrail;
  }

  public void registerObjectDetected(boolean isApollo)
  {
    Pose current = locationState.GetCurrentPose();
    current.moveUpdate((float) this.config.obstacleAvoidDistance);
    this.eventBus.post(new EventAutomapDetectedObject(current,isApollo));
  }

  public AutoSurveying.Direction getCurrentDirection()
  {
    Pose current = locationState.GetCurrentPose();
    double degree = current.getHeading();
    if (degree >= 45 && degree < 135
        || degree >= -315 && degree < -225)
      return AutoSurveying.Direction.Left;
    if (degree >= 135 && degree < 225
        || degree >= -225 && degree < -135)
      return AutoSurveying.Direction.Up;

    if (degree >= 225 && degree < 315
        || degree >= -135 && degree < -45)
      return AutoSurveying.Direction.Right;

    return AutoSurveying.Direction.Down;
  }

  public boolean isThereRadiation()
  {
    return sensorState.getColourEnum() == config.colorRadiation;
  }
}
