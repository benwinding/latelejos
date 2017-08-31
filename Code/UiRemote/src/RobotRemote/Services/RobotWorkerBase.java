package RobotRemote.Services;

import RobotRemote.Helpers.Logger;

public abstract class RobotWorkerBase implements Runnable{
  private Thread thread;
  private String threadName;
  private int msDelay;

  public RobotWorkerBase(String threadName, int msDelay) {
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
        Logger.LogCrossThread("THREAD: "+threadName+": Interupted... terminating thread");
        break;
      }
    }
    thread = null;
    OnShutdown();
  }

  protected void OnStart() { }

  protected void OnShutdown() { }

  protected abstract void Repeat();

  public void start() {
    if (thread != null) {
      Logger.WarnCrossThread("THREAD: "+threadName+": Already Started");
      return;
    }
    Logger.LogCrossThread("THREAD: "+threadName+": Started");
    thread = new Thread (this, threadName);
    thread.start ();
  }

  public void kill() {
    if (thread == null)
      return;
    Logger.LogCrossThread("THREAD: "+threadName+": About to kill");
    thread.interrupt();
  }
}
