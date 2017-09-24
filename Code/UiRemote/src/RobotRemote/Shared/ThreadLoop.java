package RobotRemote.Shared;

import java.util.concurrent.Callable;

public class ThreadLoop {
  private Thread loopThread = new Thread();
  private String threadName;

  public ThreadLoop(String threadName) {
    this.threadName = threadName;
  }

  public void StartThread(Callable repeatThis, int msLoopDelay) {
    StopThread();
    loopThread = new Thread(() -> {
      while(!loopThread.isInterrupted()) {
        try {
          repeatThis.call();
          Thread.sleep(msLoopDelay);
        } catch (Exception e) {
          Logger.debug("THREAD: Interrupted: ThreadLoop: " + loopThread.getName());
          return;
        }
      }
    });
    loopThread.setName(this.threadName);
    Logger.debug("THREAD: Starting ThreadLoop: " + loopThread.getName());
    loopThread.start();
  }

  public void StopThread() {
    loopThread.interrupt();
  }
}
