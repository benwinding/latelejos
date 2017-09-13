package RobotRemote.UIServices;

import RobotRemote.RobotServices.Movement.IMoveThread;

public class ServiceLocator {
  private IMoveThread moveThread;

  public ServiceLocator(IMoveThread moveThread) {
    this.moveThread = moveThread;
  }

  public IMoveThread getMoveThread() {
    return moveThread;
  }
}
