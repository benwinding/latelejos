package RobotRemote.RobotServices.Connection;

public class RobotConnectionState {
  private boolean isConnected = true;

  public boolean isConnected() {
    return isConnected;
  }

  void setConnected(boolean connected) {
    isConnected = connected;
  }
}
