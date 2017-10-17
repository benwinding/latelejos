package RobotRemote.UIServices.MapHandlers;

import RobotRemote.Models.MapPoint;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class NgzUtils {
  public static boolean isPointInNgzArea(MapPoint point, List<MapPoint> ngzSet) {
    Point testPoint = new Point((int)point.x,(int)point.y);
    Polygon polygon = new Polygon();
    for(MapPoint mapPoint: ngzSet) {
      polygon.addPoint((int)mapPoint.x,(int)mapPoint.y);
    }
    if(polygon.contains(testPoint))
      return true;
    return false;
  }

  public static boolean isRobotInNgzArea(MapPoint robotLocation, List<MapPoint> ngzSet, int robotLengthCm) {
    int x = (int)robotLocation.x - robotLengthCm / 2;
    int y = (int)robotLocation.y - robotLengthCm / 2;
    Rectangle2D testPoint = new Rectangle(x,y,robotLengthCm,robotLengthCm);
    Polygon polygon = new Polygon();
    for(MapPoint mapPoint: ngzSet) {
      polygon.addPoint((int)mapPoint.x,(int)mapPoint.y);
    }
    if(polygon.intersects(testPoint))
      return true;
    if(polygon.contains(testPoint))
      return true;
    return false;
  }
}
