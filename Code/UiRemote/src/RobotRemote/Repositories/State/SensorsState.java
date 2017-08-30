package RobotRemote.Repositories.State;

public class SensorsState {
  private double ultraReading;
  private double colourReading;

  public double getUltraReading() {
    return ultraReading;
  }

  public void setUltraReading(double ultraReading) {
    this.ultraReading = ultraReading;
  }

  public double getColourReading() {
    return colourReading;
  }

  public void setColourReading(double colourReading) {
    this.colourReading = colourReading;
  }
}
