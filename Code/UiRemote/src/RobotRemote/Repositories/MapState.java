package RobotRemote.Repositories;

public class MapState {
  private float zoomLevel;
  private float mapH;
  private float mapW;

  public MapState(float zoomLevel, float mapH, float mapW) {
    this.zoomLevel = zoomLevel;
    this.mapH = mapH;
    this.mapW = mapW;
  }

  public float getZoomLevel() {
    return zoomLevel;
  }

  public void setZoomLevel(float zoomLevel) {
    this.zoomLevel = zoomLevel;
  }

  public float getMapW() {
    return mapW;
  }

  public float getMapH() {
    return mapH;
  }
}
