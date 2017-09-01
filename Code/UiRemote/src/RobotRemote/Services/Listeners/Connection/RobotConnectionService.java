package RobotRemote.Services.Listeners.Connection;

import RobotRemote.Helpers.Logger;
import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import lejos.remote.ev3.RemoteRequestEV3;

public class RobotConnectionService {
  public RemoteRequestEV3 BrickInstance;

  public boolean IsConnected() {return BrickInstance != null;}

  public void InitializeBrick() {
    try {
      BrickInfo[] bricks = BrickFinder.discover();
      BrickInfo firstEv3 = bricks[0];
      BrickInstance = new RemoteRequestEV3(firstEv3.getIPAddress());
      Logger.Log("Found ev3!");
      Logger.Log("Ip address: " + firstEv3.getIPAddress());
    }
    catch (Exception e) {
      Logger.Log("No ev3 robots detected in network");
    }
  }

  public RemoteRequestEV3 GetBrick() {
    if(!IsConnected())
      InitializeBrick();
    return BrickInstance;
  }

  public void closeConnection() {
    if(!IsConnected())
      return;
    this.BrickInstance.disConnect();
  }
}
