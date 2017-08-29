package RobotRemote.Utils;

import RobotRemote.Views.Main.ManualController;
import RobotRemote.Services.CustomNavigator;

public class ThreadManager {

  public static void KillAll() {
    Logger.Log("Thread Manager: Killing all threads...");
    ManualController.mapRefreshThread.interrupt();
    CustomNavigator.moveThread.interrupt();
  }
}
