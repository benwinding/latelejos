package RobotRemote;

import RobotRemote.Models.MapPoint;
import RobotRemote.Models.MapState;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

public class MapLayerFactory {
  private final MapPoint mapSize;
  private MapState mapState;

  public MapLayerFactory(MapState mapState) {
    this.mapState = mapState;
    this.mapSize = mapState.GetMapSize();
  }

  public List<Canvas> CreateMapLayers() {
    List<Canvas> mapLayers = Arrays.asList(
        this.CreateBorderLayer(mapState.GetPointsBorder(), Color.BLACK),
        this.CreateCurrentLocationLayer(mapState.GetLastPoint()),
        this.CreateSensorFieldLayer(mapState.GetLastPoint()),
        this.CreateVisitedLayer(mapState.GetPointsVisited(), Color.GREEN)
    );
    return mapLayers;
  }

  public Canvas CreateBorderLayer(List<MapPoint> points, Color colour) {
    Canvas layer = new Canvas(mapSize.x,mapSize.y);
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
    Canvas layer = new Canvas(mapSize.x,mapSize.y);
    GraphicsContext gc = layer.getGraphicsContext2D();
    gc.setStroke(colour);
    for(int i = 0; i<points.size() - 1;i++) {
      MapPoint p1 = points.get(i);
      MapPoint p2 = points.get(i+1);
      double x1 = p1.x;
      double y1 = mapSize.y - p1.y;
      double x2 = p2.x;
      double y2 = mapSize.y -p2.y;
      gc.strokeLine(x1,y1,x2,y2);
    }
    return layer;
  }

  public Canvas CreateCurrentLocationLayer(MapPoint robotLocation) {
    int robotW = 60;
    int robotH = 50;

    Canvas layer = new Canvas(mapSize.x,mapSize.y);
    GraphicsContext gc = layer.getGraphicsContext2D();

    double x = robotLocation.x - robotW/2;
    double y = mapSize.y - robotLocation.y - robotH/2;

    double rotationCenterX = (robotW) / 2;
    double rotationCenterY = (robotH) / 2;

    gc.save();
    gc.translate(x, y);
    gc.translate(rotationCenterX, rotationCenterY);
    gc.rotate(robotLocation.theta);
    gc.translate(-rotationCenterX, -rotationCenterY);

    Image imgRobot = new Image(getClass().getResourceAsStream("../res/img/robot-map.png"));

    gc.drawImage(imgRobot,0,0, robotW, robotH);

    gc.restore();

    return layer;
  }

  public Canvas CreateSensorFieldLayer(MapPoint robotLocation) {
    int robotW = 60;
    int robotH = 180;

    Canvas layer = new Canvas(mapSize.x,mapSize.y);
    GraphicsContext gc = layer.getGraphicsContext2D();

    double x = robotLocation.x - robotW/2;
    double y = mapSize.y - robotLocation.y - robotH/2;

    double rotationCenterX = (robotW) / 2;
    double rotationCenterY = (robotH) / 2;

    gc.save();
    gc.translate(x, y);
    gc.translate(rotationCenterX, rotationCenterY);
    gc.rotate(robotLocation.theta);
    gc.translate(-rotationCenterX, -rotationCenterY);

    Image imgSensorField = new Image(getClass().getResourceAsStream("../res/img/sensor-field.png"));

    gc.drawImage(imgSensorField,0,0, robotW, robotH);
    gc.restore();

    return layer;
  }
}
