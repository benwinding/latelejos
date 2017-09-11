package RobotRemote.RobotStateMachine;

import RobotRemote.Services.RobotServiceBase;

public class RStateObjectAvoidance extends RobotServiceBase {
  public RStateObjectAvoidance() {
    super("Mode Object Avoidance", 80);
  }

  @Override
  protected void Repeat() {
  }

  @Override
  protected void OnShutdown() {

  }
}
