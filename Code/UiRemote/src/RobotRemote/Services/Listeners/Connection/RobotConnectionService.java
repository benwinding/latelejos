package RobotRemote.Services.Listeners.Connection;

import RobotRemote.Helpers.Logger;
import RobotRemote.Helpers.Synchronizer;
import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import lejos.remote.ev3.RemoteEV3;
import lejos.remote.ev3.RemoteRequestEV3;

public class RobotConnectionService {
  private RemoteRequestEV3 BrickInstanceRequest;
  private RemoteEV3 BrickInstanceRemoteEv3;

  public boolean IsConnected() {return BrickInstanceRequest != null;}

  public void InitializeBrick() {
    try {
      BrickInfo[] bricks = BrickFinder.discover();
      BrickInfo firstEv3 = bricks[0];
      BrickInstanceRequest = new RemoteRequestEV3(firstEv3.getIPAddress());
      BrickInstanceRemoteEv3 = new RemoteEV3(firstEv3.getIPAddress());
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
    return BrickInstanceRequest;
  }

  public void closeConnection() {
    if(!IsConnected())
      return;
    Synchronizer.RunNotConcurrent(() -> {
      this.BrickInstanceRequest.disConnect();
    });
  }

  public RemoteEV3 GetBrickeRemoteEv3() {
    if(!IsConnected())
      InitializeBrick();
    return this.BrickInstanceRemoteEv3;
  }
}
