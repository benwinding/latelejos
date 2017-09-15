package RobotRemote.Shared;

import javafx.scene.paint.Color;

public class ColourTranslator {
  public static String GetColourName(int colourId) {
    switch(colourId) {
      case 0: return "RED";
      case 1: return "GREEN";
      case 2: return "BLUE";
      case 3: return "YELLOW";
      case 4: return "MAGENTA";
      case 5: return "ORANGE";
      case 6: return "WHITE";
      case 7: return "BLACK";
      case 8: return "PINK";
      case 9: return "GRAY";
      case 10: return "LIGHT_GRAY";
      case 11: return "DARK_GRAY";
      case 12: return "CYAN";
      case 13: return "BROWN";
      default: return "WHITE";
    }
  }

  public static Color GetColourEnum(int colourId) {
    switch(colourId) {
      case 0: return Color.RED;
      case 1: return Color.GREEN;
      case 2: return Color.BLUE;
      case 3: return Color.YELLOW;
      case 4: return Color.MAGENTA;
      case 5: return Color.ORANGE;
      case 6: return Color.WHITE;
      case 7: return Color.BLACK;
      case 8: return Color.PINK;
      case 9: return Color.GRAY;
      default: return Color.WHITE;
    }
  }
}
