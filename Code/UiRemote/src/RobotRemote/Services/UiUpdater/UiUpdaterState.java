package RobotRemote.Services.UiUpdater;

import RobotRemote.Models.MapPoint;

import java.util.ArrayList;
import java.util.List;

public class UiUpdaterState {
  private float zoomLevel;
  private float mapH;
  private float mapW;
  private double mapDragDeltax;
  private double mapDragDeltay;

  public UiUpdaterState(float zoomLevel, float mapH, float mapW) {
    this.zoomLevel = zoomLevel;
    this.mapH = mapH;
    this.mapW = mapW;
  }

  public float getZoomLevel() {
    return zoomLevel;
  }

  public float getMapW() {
    return mapW;
  }

  public float getMapH() {
    return mapH;
  }

  public List<MapPoint> GetPointsBorder() {
    List<MapPoint> pointsMapBorder = new ArrayList<>();
    pointsMapBorder.add(new MapPoint(0,0));
    pointsMapBorder.add(new MapPoint(mapW,0));
    pointsMapBorder.add(new MapPoint(mapW,mapH));
    pointsMapBorder.add(new MapPoint(0,mapH));
    pointsMapBorder.add(new MapPoint(0,0));
    return pointsMapBorder;
  }

  public void incrementZoomLevel() {
    this.zoomLevel = (float) (this.zoomLevel * 1.2);
  }

  public void decrementZoomLevel() {
    this.zoomLevel = (float) (this.zoomLevel * 0.8);
  }

  public void zoomReset() {
    this.zoomLevel = 1f;
  }

  public void setMapDraggedDelta(double x, double y) {
    this.mapDragDeltax = x;
    this.mapDragDeltay = y;
  }

  public double getMapDragDeltaX() {
    return mapDragDeltax;
  }

  public double getMapDragDeltaY() {
    return mapDragDeltay;
  }
}
