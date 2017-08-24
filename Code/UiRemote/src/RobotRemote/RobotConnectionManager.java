package RobotRemote;

import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import lejos.remote.ev3.RemoteRequestEV3;

public class RobotConnectionManager {
  public static RemoteRequestEV3 BrickInstance;

  private static boolean IsConnected() {return BrickInstance != null;}

  public static void InitializeBrick() {
    BrickInfo[] bricks = BrickFinder.discover();
    try {
      BrickInfo firstEv3 = bricks[0];
      BrickInstance = new RemoteRequestEV3(firstEv3.getIPAddress());
      Logger.Log("Found ev3!");
      Logger.Log("Ip address: " + firstEv3.getIPAddress());
    }
    catch (Exception e) {
      Logger.Log("No ev3 robots detected in network");
    }
  }

  static RemoteRequestEV3 GetBrick() {
    if(!IsConnected())
      InitializeBrick();
    return BrickInstance;
  }
}
