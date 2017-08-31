package RobotRemote.Services.Workers.SensorService;

public class SensorsState {
  private double ultraReading;
  private double colourReadingR;
  private double colourReadingG;
  private double colourReadingB;

  public double getUltraReading() {
    return ultraReading;
  }

  public void setUltraReading(double ultraReading) {
    this.ultraReading = ultraReading;
  }

  public double getColourReadingR() {
    return colourReadingR;
  }

  public void setColourReadingR(double colourReadingR) {
    this.colourReadingR = colourReadingR;
  }

  public double getColourReadingG() {
    return colourReadingG;
  }

  public void setColourReadingG(double colourReadingG) {
    this.colourReadingG = colourReadingG;
  }

  public double getColourReadingB() {
    return colourReadingB;
  }

  public void setColourReadingB(double colourReadingB) {
    this.colourReadingB = colourReadingB;
  }
}
