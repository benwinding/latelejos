package RobotRemote.UIServices.UiUpdater;

import RobotRemote.RobotServices.Movement.Mocks.MockSensor;
import RobotRemote.Shared.ColourTranslator;
import RobotRemote.Models.MapPoint;
import RobotRemote.Shared.RobotConfiguration;
import RobotRemote.Shared.AppStateRepository;
import RobotRemote.UIServices.MapHandlers.UserNoGoZoneState;
import RobotRemote.UIServices.MapHandlers.UserWaypointsState;
import RobotRemote.RobotServices.Sensors.DiscoveredColoursState;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class MapSelectedLayersFactory {
  private final float mapH;
  private final float mapW;
  private final float mapPixelsPerCm;
  private final RobotConfiguration config;
  private DiscoveredColoursState discoveredColoursState;
  private UserWaypointsState userWaypointsState;
  private UserNoGoZoneState userNoGoZoneState;
  private UiUpdaterState uiUpdaterState;

  MapSelectedLayersFactory(RobotConfiguration config, AppStateRepository appStateRepository) {
    this.uiUpdaterState = appStateRepository.getUiUpdaterState();
    this.mapPixelsPerCm = config.mapPixelsPerCm;
    this.mapH = uiUpdaterState.getMapH()*mapPixelsPerCm;
    this.mapW = uiUpdaterState.getMapW()*mapPixelsPerCm;
    this.config = config;
    this.userNoGoZoneState = appStateRepository.getUserNoGoZoneState();
    this.userWaypointsState = appStateRepository.getUserWaypointsState();
    this.discoveredColoursState = appStateRepository.getDiscoveredColoursState();
  }

  List<Canvas> CreateMapLayers() {
    List<Canvas> mapLayers = Arrays.asList(
        this.CreateDiscoveredColoursLayer(discoveredColoursState),
        this.CreateBorderLayer(uiUpdaterState.GetPointsBorder(), config.colorBorder),
        this.CreateWaypointsLayer(userWaypointsState.GetSelectedMapPoints(), Color.BLUE),
        this.CreateObstaclesLayer(userNoGoZoneState.GetObstacles(), Color.ORANGE),
        this.CreateAppolloLayer(userNoGoZoneState.GetAppollo(), Color.DARKBLUE),
        this.CreateNgzLayer(userNoGoZoneState.GetNgzPoints())
    );
    UpdaterUtils.SetScalesOnLayers(mapLayers, config, uiUpdaterState);
    return mapLayers;
  }

  private void drawMockData(GraphicsContext gc)
  {
      UpdaterUtils.DrawAreaOnContext(gc, MockSensor.Crater, config, Color.web("BLACK",0.9));
      UpdaterUtils.DrawAreaOnContext(gc, MockSensor.Radiation, config, Color.web("GREEN",0.3));
      UpdaterUtils.DrawAreaOnContext(gc, MockSensor.Track, config, Color.web("YELLOW",0.9));
      UpdaterUtils.DrawAreaOnContext(gc, MockSensor.Track1, config, Color.web("YELLOW",0.9));
      UpdaterUtils.DrawAreaOnContext(gc, MockSensor.Apollo, config, Color.web("RED",0.9));
  }

  private void drawNGZBoundingBox(List<MapPoint> points,GraphicsContext gc)
  {
    //Draw bounding bo
    Path2D path = new Path2D.Double();
    int count=0;
    for(MapPoint mapPoint: points) {
      if(count==0)
        path.moveTo(mapPoint.x,mapPoint.y);
      else
        path.lineTo(mapPoint.x,mapPoint.y);
      count++;
    }
    Rectangle rectangle =path.getBounds();
    List<MapPoint> box = new ArrayList<>();
    box.add(new MapPoint(rectangle.x,rectangle.y));
    box.add(new MapPoint(rectangle.x+rectangle.width,rectangle.y));
    box.add(new MapPoint(rectangle.x+rectangle.width,rectangle.y+rectangle.height));
    box.add(new MapPoint(rectangle.x,rectangle.y +rectangle.height));
    UpdaterUtils.DrawAreaOnContext(gc, box, config, Color.web("BLUE",0.5));
  }

  private Canvas CreateNgzLayer(List<List<MapPoint>> pointSets) {
    Canvas layer = new Canvas(mapW*3,mapH*3);
    GraphicsContext gc = layer.getGraphicsContext2D();
    for(List<MapPoint> points: pointSets) {
     UpdaterUtils.DrawAreaOnContext(gc, points, config, Color.web("RED",0.1));
     UpdaterUtils.DrawPointsOnContext(gc, points, config, Color.web("RED"));
     UpdaterUtils.DrawCirclesOnContext(gc, points, config, Color.web("RED"), 10);

      if(config.enableTestData)
      {
        drawNGZBoundingBox(points, gc);
      }
    }

    if(config.enableTestData)
    {
      drawMockData(gc);
    }
    return layer;
  }
  private Canvas CreateBorderLayer(List<MapPoint> points, Color colour) {
    Canvas layer = new Canvas(mapW*3,mapH*3);
    GraphicsContext gc = layer.getGraphicsContext2D();
    gc.setLineWidth(10);
    UpdaterUtils.DrawPointsOnContext(gc, points, config, colour);
    return layer;
  }

  private Canvas CreateDiscoveredColoursLayer(DiscoveredColoursState discoveredColoursState) {
    Canvas layer = new Canvas(mapW*3,mapH*3);
    GraphicsContext gc = layer.getGraphicsContext2D();
    int circleSize = 10;
    for(int colourInt = 0; colourInt<10;colourInt++) {
      List<MapPoint> points = discoveredColoursState.GetPointsMatching(colourInt);
      Collections.reverse(points);
      String colourName = ColourTranslator.GetColourName(colourInt);
      Color colorCircle = Color.web(colourName);
      Color colorArea = Color.web(colourName, 0.3);
      UpdaterUtils.DrawCirclesOnContext(gc, points, config, colorCircle, circleSize);
      UpdaterUtils.DrawAreaOnContext(gc, points, config, colorArea);
    }
    return layer;
  }

  private Canvas CreateAppolloLayer(ArrayList<MapPoint> mapPoints, Color color) {
    Canvas layer = new Canvas(mapW*3,mapH*3);
    GraphicsContext gc = layer.getGraphicsContext2D();
    UpdaterUtils.DrawFilledCirclesOnContext(gc, mapPoints, config, color,40);
    return layer;
  }

  private Canvas CreateObstaclesLayer(ArrayList<MapPoint> mapPoints, Color color) {
    Canvas layer = new Canvas(mapW*3,mapH*3);
    GraphicsContext gc = layer.getGraphicsContext2D();
    UpdaterUtils.DrawFilledCirclesOnContext(gc, mapPoints, config, color,40);
    return layer;
  }

  private Canvas CreateWaypointsLayer(List<MapPoint> waypoints, Color color) {
    Canvas layer = new Canvas(mapW*3,mapH*3);
    GraphicsContext gc = layer.getGraphicsContext2D();
    UpdaterUtils.DrawCirclesOnContext(gc, waypoints, config, color,30);
    return layer;
  }
}
