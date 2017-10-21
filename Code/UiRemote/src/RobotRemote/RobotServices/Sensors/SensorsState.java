package RobotRemote.RobotServices.Sensors;

import RobotRemote.Shared.ColourTranslator;
import RobotRemote.Shared.Logger;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class SensorsState {
  private double ultraReading;
  private int colourId;
  private int arrayLength = 5;
  private int[] colourArray = new int[arrayLength];
  private int colourArrayPosition;
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
    this.colourArray[colourArrayPosition] = colourId;
    colourArrayPosition++;
    if(colourArrayPosition >= arrayLength)
      colourArrayPosition = 0;
    this.colourId = colourId;
  }

  public int getColourId() {
    return this.mostFrequent(this.colourArray);
  }

  int mostFrequent(int... ary) {
    // Taken from s.o. answer:
    // https://stackoverflow.com/questions/1852631/determine-the-most-common-occurrence-in-an-array
    Map<Integer, Integer> m = new HashMap<Integer, Integer>();

    for (int a : ary) {
      Integer freq = m.get(a);
      m.put(a, (freq == null) ? 1 : freq + 1);
    }

    int max = -1;
    int mostFrequent = -1;

    for (Map.Entry<Integer, Integer> e : m.entrySet()) {
      if (e.getValue() > max) {
        mostFrequent = e.getKey();
        max = e.getValue();
      }
    }

    return mostFrequent;
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

  public Color getColourEnum() {

    return ColourTranslator.GetColourEnum(this.colourId);
  }
}
