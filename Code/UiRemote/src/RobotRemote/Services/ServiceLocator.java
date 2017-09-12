package RobotRemote.Services;

import RobotRemote.Services.Movement.MoveThreads.IMoveThread;

public class ServiceLocator {
  private IMoveThread moveThread;

  public ServiceLocator(IMoveThread moveThread) {
    this.moveThread = moveThread;
  }

  public IMoveThread getMoveThread() {
    return moveThread;
  }
}
