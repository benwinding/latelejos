package RobotRemote.RobotServices.Sensors;

import RobotRemote.Models.MapPoint;
import RobotRemote.Shared.ColourTranslator;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class DiscoveredColoursState {
  private HashMap<Integer, ArrayList<MapPoint>> colouredPointsSeen;

  public DiscoveredColoursState() {
    colouredPointsSeen = new HashMap<>();
  }

  public ArrayList<MapPoint> GetPointsMatching(int colour) {
    if(!colouredPointsSeen.containsKey(colour)) {
      colouredPointsSeen.put(colour, new ArrayList<MapPoint>());
    }
    return colouredPointsSeen.get(colour);
  }

  public ArrayList<MapPoint> GetPointsMatching(Color color) {
    int colour = ColourTranslator.GetColourId(color);
    if(!colouredPointsSeen.containsKey(colour)) {
      colouredPointsSeen.put(colour, new ArrayList<MapPoint>());
    }
    return colouredPointsSeen.get(colour);
  }

  public void AddColouredPoint(int colour, MapPoint newColouredPoint) {
    if(ColourTranslator.GetColourEnum(colour).equals(Color.WHITE))
      return;
    if(!colouredPointsSeen.containsKey(colour)) {
      colouredPointsSeen.put(colour, new ArrayList<MapPoint>());
    }
    colouredPointsSeen.get(colour).add(newColouredPoint);
  }
}
