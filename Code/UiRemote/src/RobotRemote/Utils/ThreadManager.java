package RobotRemote.Utils;

import RobotRemote.Controllers.ManualController;
import RobotRemote.CustomNavigator;

public class ThreadManager {

  public static void KillAll() {
    Logger.Log("Thread Manager: Killing all threads...");
    ManualController.mapRefreshThread.interrupt();
    CustomNavigator.moveThread.interrupt();
  }
}
