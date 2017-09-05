package RobotRemote.Helpers;

public class Synchronizer {
  public synchronized static void RunNotConcurrent(Runnable runnable) { runnable.run();
  }
}
