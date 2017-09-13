package RobotRemote.Shared;

public class Synchronizer {
  public synchronized static void RunNotConcurrent(Runnable runnable) { runnable.run();
  }
}
