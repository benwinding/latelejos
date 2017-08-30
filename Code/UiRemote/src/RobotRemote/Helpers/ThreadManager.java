package RobotRemote.Helpers;

import RobotRemote.UI.Views.Main.ManualController;
import RobotRemote.Services.Asynchronous.Movement.CustomNavigator;

public class ThreadManager {

  public static void KillAll() {
    Logger.Log("Thread Manager: Killing all threads...");
    ManualController.mapRefreshThread.interrupt();
    CustomNavigator.moveThread.interrupt();
  }
}
