package RobotRemote.Services;

import RobotRemote.Helpers.Logger;

public abstract class RobotServiceBase implements Runnable{
  protected Thread thread;
  private String threadName;
  private int msDelay;

  public RobotServiceBase(String threadName, int msDelay) {
    this.threadName = threadName;
    this.msDelay = msDelay;
  }

  @Override
  public void run() {
    OnStart();
    while(!thread.isInterrupted()) {
      try {
        Repeat();
        Thread.sleep(msDelay);
      } catch (InterruptedException e) {
        Logger.log("THREAD: "+threadName+": Interupted... terminating thread");
        break;
      }
    }
    OnShutdown();
    thread = null;
  }

  protected void OnStart() { }

  protected void OnShutdown() { }

  protected abstract void Repeat();

  public void start() {
    if (thread != null) {
      Logger.warn("THREAD: "+threadName+": Already Started");
      return;
    }
    Logger.log("THREAD: "+threadName+": Started");
    thread = new Thread (this, threadName);
    thread.start ();
  }

  public void kill() {
    if (thread == null)
      return;
    Logger.log("THREAD: "+threadName+": About to kill");
    thread.interrupt();
  }
}
