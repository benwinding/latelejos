package RobotRemote.UIServices.UiUpdater;

import RobotRemote.Models.MapPoint;
import RobotRemote.RobotStateMachine.StateMachineBuilder;
import RobotRemote.Shared.RobotConfiguration;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

public class UpdaterUtils {
  public static void DrawPointsOnContext(GraphicsContext gc, List<MapPoint> points, RobotConfiguration config, Color colour) {
    gc.setStroke(colour);
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
  public static void DrawTextOnContext(GraphicsContext gc,MapPoint location , RobotConfiguration config, String text)
  {
    double p1x = (location.x + config.mapW)*config.mapPixelsPerCm;
    double p1y = (location.y + config.mapH)*config.mapPixelsPerCm;

    Font hiFont = new Font("arial regular", 30);
    gc.setFont(hiFont);
    gc.strokeText(text,p1x, p1y);
  }
  public static void DrawAreaOnContext(GraphicsContext gc, List<MapPoint> points, RobotConfiguration config, Color fillColour) {
    gc.setStroke(fillColour);
    gc.setFill(fillColour.brighter());
    int numPoints = points.size();
    double[] xpoints = new double[numPoints];
    double[] ypoints = new double[numPoints];
    for(int i = 0; i<numPoints;i++) {
      MapPoint point = points.get(i);
      double px = (point.x + config.mapW)*config.mapPixelsPerCm;
      double py = (point.y + config.mapH)*config.mapPixelsPerCm;
      xpoints[i] = px;
      ypoints[i] = py;
    }
    gc.fillPolygon(xpoints, ypoints, points.size());
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

  static void DrawCirclesOnContext(GraphicsContext gc, List<MapPoint> points, RobotConfiguration config, Color color, int circleSize) {
    gc.setStroke(color);
    for (MapPoint point : points) {
      double p1x = (point.x + config.mapW) * config.mapPixelsPerCm - circleSize / 2;
      double p1y = (point.y + config.mapH) * config.mapPixelsPerCm - circleSize / 2;
      gc.strokeOval(p1x, p1y, circleSize, circleSize);

    }
  }

  static void DrawFilledCirclesOnContext(GraphicsContext gc, List<MapPoint> points, RobotConfiguration config, Color color, int circleSize) {
    gc.setFill(color);
    gc.setStroke(color);
    for (MapPoint point : points) {
      double p1x = (point.x + config.mapW) * config.mapPixelsPerCm - circleSize / 2;
      double p1y = (point.y + config.mapH) * config.mapPixelsPerCm - circleSize / 2;
      gc.fillOval(p1x, p1y, circleSize, circleSize);
    }
  }
}
