package RobotRemote.RobotServices.Sensors;

import RobotRemote.Models.MapPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiscoveredColoursState {
  private HashMap<Integer, ArrayList<MapPoint>> colouredPointsSeen;

  public DiscoveredColoursState() {
    colouredPointsSeen = new HashMap<>();
  }

  public List<MapPoint> GetPointsMatching(int colour) {
    if(!colouredPointsSeen.containsKey(colour)) {
      colouredPointsSeen.put(colour, new ArrayList<MapPoint>());
    }
    return colouredPointsSeen.get(colour);
  }

  public void AddColouredPoint(int colour, MapPoint newColouredPoint) {
    if(!colouredPointsSeen.containsKey(colour)) {
      colouredPointsSeen.put(colour, new ArrayList<MapPoint>());
    }
    colouredPointsSeen.get(colour).add(newColouredPoint);
  }
}
