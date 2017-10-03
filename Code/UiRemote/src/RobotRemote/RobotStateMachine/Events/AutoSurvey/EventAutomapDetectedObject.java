package RobotRemote.RobotStateMachine.Events.AutoSurvey;

import lejos.robotics.navigation.Pose;

public class EventAutomapDetectedObject {

  private Pose detectedPosition;

  public EventAutomapDetectedObject(Pose detectedPosition) {
    this.detectedPosition = detectedPosition;
  }

  public Pose getDetectedPosition() {
    return detectedPosition;
  }
}
