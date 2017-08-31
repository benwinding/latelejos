package RobotRemote.Services.Synchronous.UiUpdater;

import RobotRemote.Models.MapPoint;

import java.util.ArrayList;
import java.util.List;

public class UiUpdaterState {
  private float zoomLevel;
  private float mapH;
  private float mapW;

  public UiUpdaterState(float zoomLevel, float mapH, float mapW) {
    this.zoomLevel = zoomLevel;
    this.mapH = mapH;
    this.mapW = mapW;
  }

  public float getZoomLevel() {
    return zoomLevel;
  }

  void setZoomLevel(float zoomLevel) {
    this.zoomLevel = zoomLevel;
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
}
