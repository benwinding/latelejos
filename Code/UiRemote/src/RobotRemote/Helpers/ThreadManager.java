package RobotRemote.Helpers;

import RobotRemote.UI.Views.RootController;
import RobotRemote.Services.Asynchronous.Movement.CustomNavigator;

public class ThreadManager {

  public static void KillAll() {
    Logger.Log("Thread Manager: Killing all threads...");
    RootController.mapRefreshThread.interrupt();
    CustomNavigator.moveThread.interrupt();
  }
}
