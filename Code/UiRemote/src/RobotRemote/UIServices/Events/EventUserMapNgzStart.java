package RobotRemote.UIServices.Events;

public class EventUserMapNgzStart {
  private double x;
  private double y;

  public EventUserMapNgzStart(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }
}
