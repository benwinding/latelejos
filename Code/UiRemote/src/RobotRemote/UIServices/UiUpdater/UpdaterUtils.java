package RobotRemote.UIServices.UiUpdater;

import RobotRemote.Models.MapPoint;
import RobotRemote.Shared.RobotConfiguration;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public class UpdaterUtils {
  public static void DrawPointsOnContext(GraphicsContext gc, List<MapPoint> points, RobotConfiguration config) {
    for(int i = 0; i<points.size() - 1;i++) {
      MapPoint p1 = points.get(i);
      MapPoint p2 = points.get(i+1);

      double p1x = (p1.x + config.mapW)*config.mapPixelsPerCm;
      double p2x = (p2.x + config.mapW)*config.mapPixelsPerCm;
      double p1y = (p1.y + config.mapH)*config.mapPixelsPerCm;
      double p2y = (p2.y + config.mapH)*config.mapPixelsPerCm;
      gc.strokeLine(p1x,p1y,p2x,p2y);
    }
  }

  public static void SetScalesOnLayers(List<Canvas> mapLayers, RobotConfiguration config, UiUpdaterState uiUpdaterState) {
    float mapPixelsPerCm = config.mapPixelsPerCm;
    float mapH = uiUpdaterState.getMapH()*mapPixelsPerCm;
    float mapW = uiUpdaterState.getMapW()*mapPixelsPerCm;
    for (Canvas layer : mapLayers) {
      layer.setScaleX(uiUpdaterState.getZoomLevel());
      layer.setScaleY(uiUpdaterState.getZoomLevel());
      layer.setTranslateX(uiUpdaterState.getMapDragDeltaX()-mapW*3/2);
      layer.setTranslateY(uiUpdaterState.getMapDragDeltaY()-mapH*3/2);
    }
  }
}
