package RobotRemote.Services.Synchronous;

import RobotRemote.Helpers.Logger;

public abstract class RobotThreadBase implements Runnable{
  private Thread thread;
  private String threadName;
  private int msDelay;

  public RobotThreadBase(String threadName, int msDelay) {
    this.threadName = threadName;
    this.msDelay = msDelay;
  }

  @Override
  public void run() {
    while(!thread.isInterrupted()) {
      try {
        Repeat();
        Thread.sleep(msDelay);
      } catch (InterruptedException e) {
        Logger.LogCrossThread("THREAD: Interupted " + threadName);
        break;
      }
    }
    OnShutdown();
  }

  protected abstract void OnShutdown();
  protected abstract void Repeat();

  public void start() {
    Logger.LogCrossThread("THREAD: Starting " + threadName);
    if (thread == null) {
      thread = new Thread (this, threadName);
      thread.start ();
    }
  }

  public void kill() {
    Logger.LogCrossThread("THREAD: Killing " + threadName);
    if (thread != null)
      thread.interrupt();
  }
}
