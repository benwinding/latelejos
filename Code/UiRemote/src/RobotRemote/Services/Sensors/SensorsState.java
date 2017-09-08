package RobotRemote.Services.Sensors;

public class SensorsState {
  private double ultraReading;
  private double colourReadingR;
  private double colourReadingG;
  private double colourReadingB;
  private int colourId;
  private float touchReading;
  private boolean statusUltra;
  private boolean statusColour;
  private boolean statusTouch;

  public double getUltraReading() {
    return ultraReading;
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

  void setTouchReading(float touchReading) {
    this.touchReading = touchReading;
  }

  public float getTouchReading() {
    return touchReading;
  }

  public boolean getStatusUltra() {
    return statusUltra;
  }

  void setStatusUltra(boolean statusUltra) {
    this.statusUltra = statusUltra;
  }

  public boolean getStatusTouch() {
    return statusTouch;
  }

  public boolean getStatusColour() {
    return statusColour;
  }

  public boolean isStatusColour() {
    return statusColour;
  }

  public void setStatusColour(boolean statusColour) {
    this.statusColour = statusColour;
  }

  public void setStatusTouch(boolean statusTouch) {
    this.statusTouch = statusTouch;
  }
}
