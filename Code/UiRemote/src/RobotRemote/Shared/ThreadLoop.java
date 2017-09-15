package RobotRemote.Shared;

public class ThreadLoop {
  private Thread loopThread = new Thread();
  private String threadName;

  public ThreadLoop(String threadName) {
    this.threadName = threadName;
  }

  public void StartThread(Runnable repeatThis, int msLoopDelay) {
    StopThread();
    loopThread = new Thread(() -> {
      while(!loopThread.isInterrupted()) {
        repeatThis.run();
        try {
          Thread.sleep(msLoopDelay);
        } catch (InterruptedException e) {
          Logger.log("---Interrupted: ThreadLoop");
          break;
        }
      }
    });
    loopThread.setName(this.threadName);
    loopThread.start();
  }

  public void StopThread() {
    loopThread.interrupt();
  }
}
