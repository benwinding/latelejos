package RobotRemote.Models.Events;

public class EventUserAddNgz {
  private double mapLocationX;
  private double mapLocationY;

  public EventUserAddNgz(double mapLocationX, double mapLocationY) {
    this.mapLocationX = mapLocationX;
    this.mapLocationY = mapLocationY;
  }

  public double getX() {
    return mapLocationX;
  }

  public double getY() {
    return mapLocationY;
  }
}
