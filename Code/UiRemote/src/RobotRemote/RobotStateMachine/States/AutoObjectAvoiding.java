package RobotRemote.RobotStateMachine.States;

import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Shared.ServiceManager;

public class AutoObjectAvoiding implements IModeState {
  private ServiceManager sm;

  public AutoObjectAvoiding(ServiceManager sm) {
    this.sm = sm;
  }

  @Override
  public void EnterState() {

  }
}
