package RobotRemote.UIServices.UiUpdater;

import RobotRemote.Models.MapPoint;

import java.util.ArrayList;
import java.util.List;

public class UiUpdaterState {
  private float zoomLevel;
  private float mapH;
  private float mapW;
  private double mapDragDeltax;
  private double mapDragDeltay;
  List<MapPoint> pointsMapBorder;

  public UiUpdaterState(float zoomLevel, float mapH, float mapW) {
    this.zoomLevel = zoomLevel;
    this.mapH = mapH;
    this.mapW = mapW;
    this.pointsMapBorder = new ArrayList<>();
    pointsMapBorder.add(new MapPoint(0,0));
    pointsMapBorder.add(new MapPoint(mapW,0));
    pointsMapBorder.add(new MapPoint(mapW,mapH));
    pointsMapBorder.add(new MapPoint(0,mapH));
    pointsMapBorder.add(new MapPoint(0,0));
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
    return this.pointsMapBorder;
  }

  public void SetBorderPoints(List<MapPoint> borderPointsSet) {
    this.pointsMapBorder = borderPointsSet;
    this.pointsMapBorder.add(borderPointsSet.get(0));
  }

  public void incrementZoomLevel() {
    this.zoomLevel = (float) (this.zoomLevel * 1.2);
  }

  public void decrementZoomLevel() {
    this.zoomLevel = (float) (this.zoomLevel * 0.8);
  }

  public void zoomReset() {
    this.zoomLevel = 0.7f;
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
