package RobotRemote.Services.Sensors;

public class SensorsState {
  private double ultraReading;
  private int colourId;
  private double colourReadingR;
  private double colourReadingG;
  private double colourReadingB;
  private boolean statusUltra;
  private boolean statusColour;

  public double getUltraReadingCm() {
    return ultraReading * 100;
  }

  void setUltraReading(double ultraReading) {
    this.ultraReading = ultraReading;
  }

  public double getColourReadingR() {
    return colourReadingR;
  }

  void setColourReadingR(double colourReadingR) {
    this.colourReadingR = colourReadingR;
  }

  public double getColourReadingG() {
    return colourReadingG;
  }

  void setColourReadingG(double colourReadingG) {
    this.colourReadingG = colourReadingG;
  }

  public double getColourReadingB() {
    return colourReadingB;
  }

  void setColourReadingB(double colourReadingB) {
    this.colourReadingB = colourReadingB;
  }

  void setColourId(int colourId) {
    this.colourId = colourId;
  }

  public int getColourId() {
    return colourId;
  }

  public boolean getStatusUltra() {
    return statusUltra;
  }

  void setStatusUltra(boolean statusUltra) {
    this.statusUltra = statusUltra;
  }

  public boolean getStatusColour() {
    return statusColour;
  }

  void setStatusColour(boolean statusColour) {
    this.statusColour = statusColour;
  }
}
