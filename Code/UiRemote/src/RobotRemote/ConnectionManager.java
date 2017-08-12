package RobotRemote;

import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import lejos.remote.ev3.RemoteEV3;

public class ConnectionManager {
  public static RemoteEV3 BrickInstance;

  private static boolean IsConnected() {return BrickInstance != null;}

  private static void InitializeBrick() {
    BrickInfo[] bricks = BrickFinder.discover();
    try {
      BrickInfo firstEv3 = bricks[0];
      BrickInstance = new RemoteEV3(firstEv3.getIPAddress());
      System.out.println("Found ev3!");
      System.out.println("Ip address: " + firstEv3.getIPAddress());
    }
    catch (Exception e) {
      System.out.println("No ev3 robots detected in network");
    }
  }

  static RemoteEV3 GetBrick() {
    if(!IsConnected())
      InitializeBrick();
    return BrickInstance;
  }
}
