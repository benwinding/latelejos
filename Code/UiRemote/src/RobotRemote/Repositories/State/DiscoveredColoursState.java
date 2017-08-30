package RobotRemote.Repositories.State;

import RobotRemote.Models.MapPoint;
import javafx.scene.paint.Color;

import java.util.*;

public class DiscoveredColoursState {
  private HashMap<Color, List<MapPoint>> colouredPointsSeen;

  public DiscoveredColoursState() {
    colouredPointsSeen = new HashMap<>();
  }

  public List<MapPoint> GetPointsMatching(Color colour) {
    if(!colouredPointsSeen.containsKey(colour)) {
      colouredPointsSeen.put(colour, new ArrayList<MapPoint>());
    }
    return colouredPointsSeen.get(colour);
  }

  public void AddColouredPoint(Color colour, MapPoint newColouredPoint) {
    if(!colouredPointsSeen.containsKey(colour)) {
      colouredPointsSeen.put(colour, new ArrayList<MapPoint>());
    }
    colouredPointsSeen.get(colour).add(newColouredPoint);
  }
}
