package RobotRemote.Services.Workers.UiUpdater;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.Events.EventUserAddNgz;
import RobotRemote.Models.MapPoint;
import RobotRemote.Services.Listeners.Movement.LocationState;
import RobotRemote.Services.Listeners.StateMachine.UserNoGoZoneState;
import com.google.common.eventbus.EventBus;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import lejos.utility.Matrix;

import java.util.Arrays;
import java.util.List;

class MapLayerFactory {
  private final float mapH;
  private final float mapW;
  private LocationState locationState;
  private UserNoGoZoneState userNoGoZoneState;
  private EventBus eventBus;
  private UiUpdaterState uiUpdaterState;

  MapLayerFactory(EventBus eventBus, UiUpdaterState uiUpdaterState, LocationState locationState, UserNoGoZoneState userNoGoZoneState) {
    this.eventBus = eventBus;
    this.uiUpdaterState = uiUpdaterState;
    this.mapH = uiUpdaterState.getMapH();
    this.mapW = uiUpdaterState.getMapW();
    this.locationState = locationState;
    this.userNoGoZoneState = userNoGoZoneState;
  }

  List<Canvas> CreateMapLayers() {
    List<Canvas> mapLayers = Arrays.asList(
        this.CreateBorderLayer(uiUpdaterState.GetPointsBorder(), Color.BLACK),
        this.CreateCurrentLocationLayer(locationState.GetCurrentPosition()),
        this.CreateSensorFieldLayer(locationState.GetCurrentPosition()),
        this.CreateVisitedLayer(locationState.GetPointsVisited(), Color.GREEN),
        this.CreateGridLayer(
          userNoGoZoneState.getNgzMatrix(),
          Color.web("red",0.2),
          Color.web("green",0.1)
        )
    );
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
    layer.setOnMousePressed((mouseEvent) -> {
      Logger.LogCrossThread("Event: Mouse click being posted");
      this.eventBus.post(new EventUserAddNgz(mouseEvent.getX(), mouseEvent.getY()));
    });
    return layer;
  }

  Canvas CreateBorderLayer(List<MapPoint> points, Color colour) {
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

  public Canvas CreateVisitedLayer(List<MapPoint> points, Color colour) {
    Canvas layer = new Canvas(mapW,mapH);
    GraphicsContext gc = layer.getGraphicsContext2D();
    gc.setStroke(colour);
    gc.setLineWidth(5);
    for(int i = 0; i<points.size() - 1;i++) {
      MapPoint p1 = points.get(i);
      MapPoint p2 = points.get(i+1);

      double x1 = p1.x;
      double x2 = p2.x;
      double y1 = mapH - p1.y;
      double y2 = mapH -p2.y;
      gc.strokeLine(x1,y1,x2,y2);
    }
    return layer;
  }

  Canvas CreateCurrentLocationLayer(MapPoint robotLocation) {
    int robotW = 60;
    int robotH = 50;

    Canvas layer = new Canvas(mapW,mapH);
    GraphicsContext gc = layer.getGraphicsContext2D();

    double x = robotLocation.x - robotW/2;
    double y = mapH - robotLocation.y - robotH/2;

    double rotationCenterX = (robotW) / 2;
    double rotationCenterY = (robotH) / 2;

    gc.save();
    gc.translate(x, y);
    gc.translate(rotationCenterX, rotationCenterY);
    gc.rotate(360-robotLocation.theta-90);
    gc.translate(-rotationCenterX, -rotationCenterY);

    Image imgRobot = new Image(getClass().getResourceAsStream("./Images/robot-map.png"));

    gc.drawImage(imgRobot,0,0, robotW, robotH);

    gc.restore();

    return layer;
  }

  Canvas CreateSensorFieldLayer(MapPoint robotLocation) {
    int robotW = 60;
    int robotH = 180;

    Canvas layer = new Canvas(mapW,mapH);
    GraphicsContext gc = layer.getGraphicsContext2D();

    double x = robotLocation.x - robotW/2;
    double y = mapH - robotLocation.y - robotH/2;

    double rotationCenterX = (robotW) / 2;
    double rotationCenterY = (robotH) / 2;

    gc.save();
    gc.translate(x, y);
    gc.translate(rotationCenterX, rotationCenterY);
    gc.rotate(360-robotLocation.theta-90);
    gc.translate(-rotationCenterX, -rotationCenterY);

    Image imgSensorField = new Image(getClass().getResourceAsStream("./Images/sensor-field.png"));

    gc.drawImage(imgSensorField,0,0, robotW, robotH);
    gc.restore();

    return layer;
  }
}
