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


    if (!sm.getAppState().getSensorsState().getStatusColour())
    {
      if (sm.getAppState().getLocationState().GetCurrentPosition().y < 0 || sm.getAppState().getLocationState().GetCurrentPosition().x < 0 || sm.getAppState().getLocationState().GetCurrentPosition().y > 85 || sm.getAppState().getLocationState().GetCurrentPosition().x > 60)
        return true;
      return false;
    }

    javafx.scene.paint.Color color = sensorState.getColourEnum();
    return color == config.colorBorder;
  }

  public boolean isThereACrater()
  {
    if (!sm.getAppState().getSensorsState().getStatusColour())
      return false;

    return sensorState.getColourEnum() == config.colorCrater;
  }

  public boolean isThereApollo()
  {
    if (!sm.getAppState().getSensorsState().getStatusColour())
      return false;

    return sensorState.getColourEnum() == config.colorApollo;
  }

  public boolean isThereATrail()
  {

    if (!sm.getAppState().getSensorsState().getStatusColour())
      return false;

    Color color = sensorState.getColourEnum();
    return color == config.colorTrail;
  }

  public void RegisterObjectDetected()
  {
    Pose current = locationState.GetCurrentPose();
    current.moveUpdate((float) this.config.obstacleAvoidDistance);
    this.eventBus.post(new EventAutomapDetectedObject(current));
  }

  public AutoSurveying.Direction getCurrentDirection()
  {
    Pose current = locationState.GetCurrentPose();
    int degree =(int)Math.abs(current.getHeading());
    switch ( degree)
    {
      case 0:
        return  AutoSurveying.Direction.Down;
      case 90:
        return  AutoSurveying.Direction.Right;
      case 180:
        return  AutoSurveying.Direction.Up;
      case 270:
        return  AutoSurveying.Direction.Left;

    }
    return AutoSurveying.Direction.Up;
  }

}
