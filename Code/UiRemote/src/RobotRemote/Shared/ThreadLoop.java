package RobotRemote.Shared;

public class ThreadLoop {
  private Thread loopThread;

  public void StartThread(Runnable runThis, int msLoopDelay) {
    loopThread = new Thread(() -> {
      while(!loopThread.isInterrupted()) {
        runThis.run();
        try {
          Thread.sleep(msLoopDelay);
        } catch (InterruptedException e) {
          break;
        }
      }
    });
    loopThread.start();
  }

  public void StopThread() {
    if(loopThread != null)
      loopThread.interrupt();
  }
}
