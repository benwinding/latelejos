package RobotRemote.RobotStateMachine.Events.AutoSurvey;

import RobotRemote.Models.MapPoint;
import lejos.robotics.navigation.Pose;

import java.util.Map;

public class EventAutomapDetectedObject {

  private Pose detectedPosition;

  public EventAutomapDetectedObject(Pose detectedPosition) {
    this.detectedPosition = detectedPosition;
  }

  public Pose getDetectedPosition() {
    return detectedPosition;
  }
}
