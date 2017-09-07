package RobotRemote.Services.UiUpdater;

import RobotRemote.Helpers.ColourTranslator;
import RobotRemote.Models.MapPoint;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.Services.MapHandlers.UserNoGoZoneState;
import RobotRemote.Services.MapHandlers.UserWaypointsState;
import RobotRemote.Services.Sensors.DiscoveredColoursState;
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
  private DiscoveredColoursState discoveredColoursState;
  private UserWaypointsState userWaypointsState;
  private UserNoGoZoneState userNoGoZoneState;
  private UiUpdaterState uiUpdaterState;

  MapSelectedLayersFactory(AppStateRepository appStateRepository) {
    this.uiUpdaterState = appStateRepository.getUiUpdaterState();
    this.mapH = uiUpdaterState.getMapH();
    this.mapW = uiUpdaterState.getMapW();
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
    float zoom = uiUpdaterState.getZoomLevel();
    for (Canvas layer : mapLayers) {
      layer.setScaleX(zoom);
      layer.setScaleY(zoom);
    }
    return mapLayers;
  }

  private Canvas CreateGridLayer(Matrix matrix, Color colourOn, Color colourOff) {
    Canvas layer = new Canvas(mapW,mapH);
    GraphicsContext gc = layer.getGraphicsContext2D();
    gc.setStroke(colourOff.darker());
    int cols = matrix.getColumnDimension();
    int rows = matrix.getRowDimension();
    float w = (mapW /rows);
    float h = (mapH /cols);
    for(int i=0;i<rows; i++) {
      for(int j=0;j<cols; j++) {
        float x1 = i*w;
        float y1 = j*h;
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
    Canvas layer = new Canvas(mapW,mapH);
    GraphicsContext gc = layer.getGraphicsContext2D();
    gc.setStroke(colour);
    for(int i = 0; i<points.size() - 1;i++) {
      MapPoint p1 = points.get(i);
      MapPoint p2 = points.get(i+1);
      gc.strokeLine(p1.x,p1.y,p2.x,p2.y);
    }
    return layer;
  }

  private Canvas CreateDiscoveredColoursLayer(DiscoveredColoursState discoveredColoursState) {
    Canvas layer = new Canvas(mapW,mapH);
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
        gc.fillOval(point.x-circleSize/2, point.y-circleSize/2, circleSize, circleSize);
      }
    }
    return layer;
  }

  private Canvas CreateWaypointsLayer(List<Waypoint> waypoints, Color color) {
    Canvas layer = new Canvas(mapW,mapH);
    GraphicsContext gc = layer.getGraphicsContext2D();

    int circleSize = 30;
    gc.setStroke(color);
    for (Waypoint point : waypoints) {
      gc.strokeOval(point.x-circleSize/2, point.y-circleSize/2, circleSize, circleSize);
    }

    return layer;
  }
}
