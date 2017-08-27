package RobotRemote;

import RobotRemote.Models.MapPoint;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class MapUiDrawer {
  public static void DrawPoints(GraphicsContext mapUi, List<MapPoint> points, Color colour) {
    mapUi.setStroke(colour);
    for(int i = 0; i<points.size() - 1;i++) {
      MapPoint p1 = points.get(i);
      MapPoint p2 = points.get(i+1);
      mapUi.strokeLine(p1.x,p1.y,p2.x,p2.y);
    }
  }

  public static void DrawPoints(GraphicsContext mapUi, MapPoint mapSize, List<MapPoint> points, Color colour) {
    mapUi.setStroke(colour);
    for(int i = 0; i<points.size() - 1;i++) {
      MapPoint p1 = points.get(i);
      MapPoint p2 = points.get(i+1);
      double x1 = p1.x;
      double y1 = mapSize.y - p1.y;
      double x2 = p2.x;
      double y2 = mapSize.y -p2.y;
      mapUi.strokeLine(x1,y1,x2,y2);
    }
  }
}
