package RobotRemote.RobotServices.Connection;

import RobotRemote.Shared.Logger;
import RobotRemote.Shared.Synchronizer;
import RobotRemote.Shared.AppStateRepository;
import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import lejos.remote.ev3.RemoteEV3;
import lejos.remote.ev3.RemoteRequestEV3;

public class RobotConnectionService {
  private final RobotConnectionState robotConnectionState;
  private RemoteRequestEV3 BrickInstanceRequest;
  private RemoteEV3 BrickInstanceRemoteEv3;

  public RobotConnectionService(AppStateRepository appStateRepository) {
    this.robotConnectionState = appStateRepository.getRobotConnectionState();
  }

  public boolean IsConnected() {return BrickInstanceRequest != null;}

  public void InitializeBrick() {
    try {
      BrickInfo[] bricks = BrickFinder.discover();
      BrickInfo firstEv3 = bricks[0];
      BrickInstanceRequest = new RemoteRequestEV3(firstEv3.getIPAddress());
      BrickInstanceRemoteEv3 = new RemoteEV3(firstEv3.getIPAddress());
      Logger.debug("Found ev3!");
      Logger.debug("Ip address: " + firstEv3.getIPAddress());
      this.robotConnectionState.setConnected(true);
    }
    catch (Exception e) {
      Logger.warn("No ev3 robots detected in network");
      this.robotConnectionState.setConnected(false);
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
    Synchronizer.SerializeRobotCalls(() -> {
      this.BrickInstanceRequest.disConnect();
    });
  }

  public RemoteEV3 GetBrickeRemoteEv3() {
    if(!IsConnected())
      InitializeBrick();
    return this.BrickInstanceRemoteEv3;
  }
}
