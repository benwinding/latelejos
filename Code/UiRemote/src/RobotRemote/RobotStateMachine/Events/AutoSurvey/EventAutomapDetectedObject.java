package RobotRemote.RobotStateMachine.Events.AutoSurvey;

import RobotRemote.Models.MapPoint;
import lejos.robotics.navigation.Pose;

import java.util.Map;

public class EventAutomapDetectedObject {

  private Pose detectedPosition;
  public Boolean isApollo;

  public EventAutomapDetectedObject(Pose detectedPosition) {
    this.detectedPosition = detectedPosition;
    isApollo = false;
  }

  public EventAutomapDetectedObject(Pose detectedPosition,boolean isApollo) {
    this.detectedPosition = detectedPosition;
    this.isApollo = isApollo;
  }
  public Pose getDetectedPosition() {
    return detectedPosition;
  }
}
