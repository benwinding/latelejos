package RobotRemote.UIServices.UiUpdater;

import RobotRemote.Models.MapPoint;
import RobotRemote.Shared.RobotConfiguration;
import RobotRemote.Shared.AppStateRepository;
import RobotRemote.RobotServices.Movement.LocationState;
import RobotRemote.RobotServices.Sensors.SensorsState;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

class MapLocationsLayersFactory {
  private final float mapH;
  private final float mapW;
  private final float mapPixelsPerCm;
  private SensorsState sensorState;
  private LocationState locationState;
  private UiUpdaterState uiUpdaterState;

  MapLocationsLayersFactory(RobotConfiguration config, AppStateRepository appStateRepository) {
    this.mapPixelsPerCm = config.mapPixelsPerCm;
    this.uiUpdaterState = appStateRepository.getUiUpdaterState();
    this.mapH = uiUpdaterState.getMapH() * mapPixelsPerCm;
    this.mapW = uiUpdaterState.getMapW() * mapPixelsPerCm;
    this.locationState = appStateRepository.getLocationState();
    this.sensorState = appStateRepository.getSensorsState();
  }

  List<Canvas> CreateMapLayers() {
    List<Canvas> mapLayers = Arrays.asList(
        this.CreateCurrentLocationLayer(locationState.GetCurrentPosition()),
        this.CreateVisitedLayer(locationState.GetPointsVisited(), Color.GREEN)
    );
    float zoom = uiUpdaterState.getZoomLevel();
    for (Canvas layer : mapLayers) {
      layer.setScaleX(zoom);
      layer.setScaleY(zoom);
      layer.setTranslateX(uiUpdaterState.getMapDragDeltaX()-mapW*3/2);
      layer.setTranslateY(uiUpdaterState.getMapDragDeltaY()-mapH*3/2);
    }
    return mapLayers;
  }

  private Canvas CreateVisitedLayer(List<MapPoint> points, Color colour) {
    Canvas layer = new Canvas(mapW*3,mapH*3);
    GraphicsContext gc = layer.getGraphicsContext2D();
    gc.setStroke(colour);
    gc.setLineWidth(5);
    for(int i = 0; i<points.size() - 1;i++) {
      MapPoint p1 = points.get(i);
      MapPoint p2 = points.get(i+1);

      double x1 = p1.x * mapPixelsPerCm + mapW;
      double x2 = p2.x * mapPixelsPerCm + mapW;
      double y1 = p1.y * mapPixelsPerCm + mapH;
      double y2 = p2.y * mapPixelsPerCm + mapH;
      gc.strokeLine(x1,y1,x2,y2);
    }
    return layer;
  }

  private Canvas CreateCurrentLocationLayer(MapPoint robotLocation) {
    double robotW = 12 * mapPixelsPerCm;
    double robotH = 10 * mapPixelsPerCm;

    Canvas layer = new Canvas(mapW*3,mapH*3);
    GraphicsContext gc = layer.getGraphicsContext2D();

    double x = (robotLocation.x * mapPixelsPerCm - robotW/2)+mapW;
    double y = (robotLocation.y * mapPixelsPerCm - robotH/2)+mapH;

    double rotationCenterX = (robotW / 2);
    double rotationCenterY = (robotH / 2);

    gc.save();
    gc.translate(x, y);
    gc.translate(rotationCenterX, rotationCenterY);
    gc.rotate((robotLocation.theta-90)+180);
    gc.translate(-rotationCenterX, -rotationCenterY);

    Image imgRobot = new Image(getClass().getResourceAsStream("../../UI/Images/robot-map.png"));

    gc.drawImage(imgRobot,0,0, robotW, robotH);
    gc.restore();

    return layer;
  }

  private Canvas CreateSensorFieldLayer(MapPoint robotLocation) {
    double sensorFieldW = 12 * mapPixelsPerCm;
    double sensorValUltra = sensorState.getUltraReadingCm();
    if(sensorValUltra < 0)
      sensorValUltra = 0;

    int sensorFieldH = (int) sensorValUltra;

    Canvas layer = new Canvas(mapW*3,mapH*3);
    GraphicsContext gc = layer.getGraphicsContext2D();

    double x = (robotLocation.x + mapW - sensorFieldW/2) * mapPixelsPerCm;
    double y = (robotLocation.y + mapH - sensorFieldH/2) * mapPixelsPerCm;

    double rotationCenterX = ((sensorFieldW) / 2) * mapPixelsPerCm;
    double rotationCenterY = ((sensorFieldH) / 2) * mapPixelsPerCm;

    gc.save();
    gc.translate(x, y );
    gc.translate(rotationCenterX, rotationCenterY);
    gc.rotate((robotLocation.theta-90)+180);
    gc.translate(-rotationCenterX, -rotationCenterY);

    Image imgSensorField = new Image(getClass().getResourceAsStream("../../UI/Images/sensor-field.png"));

    gc.drawImage(imgSensorField,0,0, sensorFieldW, sensorFieldH);
    gc.restore();

    return layer;
  }
}
