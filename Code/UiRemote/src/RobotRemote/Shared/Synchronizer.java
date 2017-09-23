package RobotRemote.Shared;

public class Synchronizer {
  public synchronized static void SerializeRobotCalls(Runnable runnable) { runnable.run();
  }
}
