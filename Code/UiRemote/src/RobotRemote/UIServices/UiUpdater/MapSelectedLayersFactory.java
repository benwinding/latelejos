package RobotRemote.UIServices.UiUpdater;

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
import lejos.robotics.navigation.Waypoint;
import lejos.utility.Matrix;

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
        this.CreateBorderLayer(uiUpdaterState.GetPointsBorder(), Color.BLACK),
        this.CreateWaypointsLayer(userWaypointsState.GetSelectedWayPoints(), Color.BLUE),
        this.CreateDiscoveredColoursLayer(discoveredColoursState),
        this.CreateGridLayer(
          userNoGoZoneState.getNgzMatrix(),
          Color.web("red",0.2),
          Color.web("green",0.1)
        )
    );
    UpdaterUtils.SetScalesOnLayers(mapLayers, config, uiUpdaterState);
    return mapLayers;
  }

  private Canvas CreateGridLayer(Matrix matrix, Color colourOn, Color colourOff) {
    Canvas layer = new Canvas(mapW*3,mapH*3);
    GraphicsContext gc = layer.getGraphicsContext2D();
    gc.setStroke(colourOff.darker());
    int cols = matrix.getColumnDimension();
    int rows = matrix.getRowDimension();
    float w = (mapW /rows);
    float h = (mapH /cols);
    for(int i=0;i<rows; i++) {
      for(int j=0;j<cols; j++) {
        float x1 = i*w + mapW;
        float y1 = j*h + mapH;
        gc.strokeRect(x1,y1,w,h);
        if(matrix.get(i,j) == 1){
          gc.setFill(colourOn);
          gc.fillRect(x1,y1,w,h);
        }
        else {
          gc.setFill(colourOff);
          gc.fillRect(x1,y1,w,h);
        }
      }
    }
    return layer;
  }

  private Canvas CreateBorderLayer(List<MapPoint> points, Color colour) {
    Canvas layer = new Canvas(mapW*3,mapH*3);
    GraphicsContext gc = layer.getGraphicsContext2D();
    gc.setStroke(colour);
    UpdaterUtils.DrawPointsOnContext(gc, points, config);
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
      Color pointColour = Color.web(colourName, 0.5);
      gc.setStroke(pointColour);
      gc.setFill(pointColour);
      for (MapPoint point: points) {
        double p1x = (point.x * mapPixelsPerCm)-circleSize/2 + mapW;
        double p1y = (point.y * mapPixelsPerCm)-circleSize/2 + mapH;
        gc.fillOval(p1x, p1y, circleSize, circleSize);
      }
    }
    return layer;
  }

  private Canvas CreateWaypointsLayer(List<Waypoint> waypoints, Color color) {
    Canvas layer = new Canvas(mapW*3,mapH*3);
    GraphicsContext gc = layer.getGraphicsContext2D();

    int circleSize = 30;
    gc.setStroke(color);
    for (Waypoint point : waypoints) {
      double p1x = (point.x + config.mapW)*config.mapPixelsPerCm - circleSize/2;
      double p1y = (point.y + config.mapH)*config.mapPixelsPerCm - circleSize/2;
      gc.strokeOval(p1x, p1y, circleSize, circleSize);
    }

    return layer;
  }
}
