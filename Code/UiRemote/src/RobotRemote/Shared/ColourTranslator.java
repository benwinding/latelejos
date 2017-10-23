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
      case 10: return "PURPLE";
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
      case 10: return Color.PURPLE;
      default: return Color.WHITE;
    }
  }


  public static int GetColourId(Color colour) {
    if (colour.equals(Color.RED)) {
      return 0;
    } else if (colour.equals(Color.GREEN)) {
      return 1;
    } else if (colour.equals(Color.BLUE)) {
      return 2;
    } else if (colour.equals(Color.YELLOW)) {
      return 3;
    } else if (colour.equals(Color.MAGENTA)) {
      return 4;
    } else if (colour.equals(Color.ORANGE)) {
      return 5;
    } else if (colour.equals(Color.WHITE)) {
      return 6;
    } else if (colour.equals(Color.BLACK)) {
      return 7;
    } else if (colour.equals(Color.PINK)) {
      return 8;
    } else if (colour.equals(Color.GRAY)) {
      return 9;
    } else if (colour.equals(Color.PURPLE)) {
      return 10;
    } else {
      return 6;
    }
  }
}
