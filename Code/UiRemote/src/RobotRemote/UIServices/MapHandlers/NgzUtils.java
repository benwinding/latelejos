package RobotRemote.UIServices.MapHandlers;

import RobotRemote.Models.MapPoint;
import RobotRemote.RobotStateMachine.States.AutoMode.AutoSurveying;
import RobotRemote.Shared.AppStateRepository;
import RobotRemote.Shared.RobotConfiguration;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class NgzUtils {
  public static boolean isPointInNgzArea(MapPoint point, List<MapPoint> ngzSet) {
    Point testPoint = new Point((int)point.x,(int)point.y);
    Path2D path = new Path2D.Double();
    int count=0;
    for(MapPoint mapPoint: ngzSet) {
      if(count==0)
        path.moveTo(mapPoint.x,mapPoint.y);
      else
        path.lineTo(mapPoint.x,mapPoint.y);
      count++;
    }
    if(path.contains(testPoint))
      return true;
    return false;
  }


  public static Rectangle getInterceptNgzArea(AppStateRepository appState, RobotConfiguration config)
  {
      int robotLong = config.robotPhysicalLength;
      for(List<MapPoint> ngzSet: appState.getUserNoGoZoneState().GetNgzPoints()) {
        Rectangle intercept =NgzUtils.getInterceptNgzArea(appState.getLocationState().GetCurrentPosition(), ngzSet, robotLong);
        if(intercept!=null)
          return intercept;
      }
      return null;
  }

  public static Rectangle getInterceptNgzArea(MapPoint robotLocation, List<MapPoint> ngzSet, int robotLengthCm) {
    int x = (int)robotLocation.x - robotLengthCm / 2;
    int y = (int)robotLocation.y - robotLengthCm / 2;
    Rectangle2D testPoint = new Rectangle(x,y,robotLengthCm,robotLengthCm);
    Path2D path = new Path2D.Double();
    int count=0;
    for(MapPoint mapPoint: ngzSet) {
      if(count==0)
        path.moveTo(mapPoint.x,mapPoint.y);
      else
        path.lineTo(mapPoint.x,mapPoint.y);
      count++;
    }
    if(path.intersects(testPoint))
      return path.getBounds();
    if(path.contains(testPoint))
      return path.getBounds();
    return null;
  }

  public static boolean isRobotInNgzArea(MapPoint robotLocation, List<MapPoint> ngzSet, double robotLengthCm, double robotWidth) {
    double x = robotLocation.x - robotLengthCm / 2;
    double y = robotLocation.y - robotLengthCm / 2;
    Rectangle2D testPoint = new Rectangle2D.Double(x,y,robotLengthCm,robotWidth);
    Path2D path = new Path2D.Double();
    int count=0;
    for(MapPoint mapPoint: ngzSet) {
      if(count==0)
        path.moveTo(mapPoint.x,mapPoint.y);
      else
        path.lineTo(mapPoint.x,mapPoint.y);
      count++;
    }
    if(path.intersects(testPoint))
      return true;
    if(path.contains(testPoint))
      return true;
    return false;
  }
}
