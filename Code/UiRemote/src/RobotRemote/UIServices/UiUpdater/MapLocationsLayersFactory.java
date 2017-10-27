package RobotRemote.UIServices.UiUpdater;

import RobotRemote.Models.MapPoint;
import RobotRemote.Shared.RobotConfiguration;
import RobotRemote.Shared.AppStateRepository;
import RobotRemote.RobotServices.Movement.LocationState;
import RobotRemote.UIServices.MapHandlers.UserNoGoZoneState;
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
  private UserNoGoZoneState ngzState;
  private LocationState locationState;
  private UiUpdaterState uiUpdaterState;
  private RobotConfiguration config;
  private AppStateRepository appStateRepository;


  MapLocationsLayersFactory(RobotConfiguration config, AppStateRepository appStateRepository) {
    this.uiUpdaterState = appStateRepository.getUiUpdaterState();
    this.config = config;
    this.mapPixelsPerCm = config.mapPixelsPerCm;
    this.mapH = uiUpdaterState.getMapH() * mapPixelsPerCm;
    this.mapW = uiUpdaterState.getMapW() * mapPixelsPerCm;
    this.locationState = appStateRepository.getLocationState();
    this.ngzState = appStateRepository.getUserNoGoZoneState();
    this.appStateRepository = appStateRepository;
  }

  List<Canvas> CreateMapLayers() {
    List<Canvas> mapLayers = Arrays.asList(
        this.CreateVisitedLayer(locationState.GetPointsVisited(), Color.web("YELLOW", 0.15)),
        this.CreateCurrentLocationLayer(locationState.GetCurrentPosition())
    );
    UpdaterUtils.SetScalesOnLayers(mapLayers, config, uiUpdaterState);
    return mapLayers;
  }

  private Canvas CreateVisitedLayer(List<MapPoint> points, Color colour) {
    Canvas layer = new Canvas(mapW*3,mapH*3);
    GraphicsContext gc = layer.getGraphicsContext2D();
    UpdaterUtils.DrawFilledCirclesOnContext(gc, points, config, colour, 50);
    return layer;
  }

  private Canvas CreateCurrentLocationLayer(MapPoint robotLocation) {
    double robotW = config.robotPhysicalWidth * mapPixelsPerCm;
    double robotH = config.robotPhysicalLength * mapPixelsPerCm;

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

    Color c =Color.web("BLUE",0.3);
    if(config.enableTestData)
    {
      gc.setFill(c);
      gc.fillRect(0,0, robotW, robotH);
    }

    if(ngzState.isRobotInNgz(robotLocation, config) || isThereABorder() ||isThereACrater() || isThereAnObject() ) {
      c =Color.web("RED",0.3);
      gc.setFill(c);
      gc.fillRect(0,0, robotW, robotH);
    }

    gc.restore();
    return layer;
  }


  public boolean isThereAnObject()
  {
    if (!this.appStateRepository.getSensorsState().getStatusUltra())
      return false;
    return this.appStateRepository.getSensorsState().getUltraReadingCm() <= config.obstacleAvoidDistance;
  }

  private boolean isThereABorder()
  {
    javafx.scene.paint.Color color = this.appStateRepository.getSensorsState().getColourEnum();
    return color == config.colorBorder;
  }

  private boolean isThereACrater()
  {
    return this.appStateRepository.getSensorsState().getColourEnum() == config.colorCrater;
  }

}
