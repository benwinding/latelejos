package RobotRemote.UIServices.MapHandlers;

import RobotRemote.Shared.Logger;
import RobotRemote.Shared.ServiceManager;
import RobotRemote.UIServices.Events.*;
import RobotRemote.Shared.RobotConfiguration;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.Lunarovermap;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.XmlTranslator;
import RobotRemote.UIServices.UiUpdater.UiUpdaterState;
import com.google.common.eventbus.Subscribe;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MapInputEventHandlers {
  private final UserWaypointsState userWaypointsState;
  private final UserNoGoZoneState userNoGoZoneState;
  private final UiUpdaterState uiUpdaterState;
  private RobotConfiguration config;

  public MapInputEventHandlers(ServiceManager sm) {
    sm.getEventBus().register(this);
    this.config = config;
    this.userNoGoZoneState = sm.getAppState().getUserNoGoZoneState();
    this.userWaypointsState = sm.getAppState().getUserWaypointsState();
    this.uiUpdaterState = sm.getAppState().getUiUpdaterState();
  }

  @Subscribe
  public void OnMapImport(EventMapImport event) {
    File importedMapFile = event.getSelectedMapFile();
    try {
      Lunarovermap mapObject = new XmlTranslator().createMapObject(importedMapFile.getName());
    } catch (JAXBException e) {
      Logger.warn("Could not translate xml to map object");
    }
  }

  @Subscribe
  public void OnMapExport(EventMapExport event) {
    File exportMapFile = event.getSelectedExportMapFile();
    Lunarovermap currentMap = null;
    try {
      String mapObject = new XmlTranslator().createXml(currentMap);
      SaveFile(mapObject, exportMapFile);
    } catch (JAXBException e) {
      Logger.warn("Could not export map object to XML");
    }
  }

  private void SaveFile(String content, File file){
    try {
      FileWriter fileWriter = new FileWriter(file);
      fileWriter.write(content);
      fileWriter.close();
    } catch (IOException ex) {
      Logger.warn("Could not save File");
    }
  }

  @Subscribe
  public void OnUserAddWaypoint(EventUserAddWaypoint event) {
    // Account for zoom on map
    float mapH = uiUpdaterState.getMapH();
    float mapW = uiUpdaterState.getMapW();
    float zoomLevel = uiUpdaterState.getZoomLevel();

    // Mouse relative coordinates to scaled map
    double mouseX = event.getX()/config.mapPixelsPerCm - ((1-zoomLevel)/2)*mapW;
    double mouseY = event.getY()/config.mapPixelsPerCm - ((1-zoomLevel)/2)*mapH;

    // Scale mouse to actual map xy coordinates
    double scaleX = mouseX / zoomLevel;
    double scaleY = mouseY / zoomLevel;
    Logger.debug(String.format("Received UserAddWaypoint:: x:%.1f, y:%.1f", scaleX, scaleY));

    userWaypointsState.AddWayPoint(scaleX,scaleY);
  }

  @Subscribe
  public void OnUserAddNgz(EventUserAddNgz event) {
    // Account for zoom on map
    float mapH = uiUpdaterState.getMapH();
    float mapW = uiUpdaterState.getMapW();
    float zoomLevel = uiUpdaterState.getZoomLevel();

    // Mouse relative coordinates to scaled map
    double mouseX = event.getX()/config.mapPixelsPerCm - ((1-zoomLevel)/2)*mapW;
    double mouseY = event.getY()/config.mapPixelsPerCm - ((1-zoomLevel)/2)*mapH;

    // Scale mouse to actual map xy coordinates
    double scaleX = mouseX / zoomLevel;
    double scaleY = mouseY / zoomLevel;

    Logger.debug(String.format("Received UserAddNGZ:: x:%.1f, y:%.1f", scaleX, scaleY));
    int cols = userNoGoZoneState.countGridRows();
    int rows = userNoGoZoneState.countGridCols();

    int r = this.GetCellInRange(mapW, cols, scaleX);
    int c = this.GetCellInRange(mapH, rows, scaleY);
    userNoGoZoneState.switchNgzCell(r,c);
  }

  @Subscribe
  public void OnUserMapDragged(EventUserMapDragged event) {
    uiUpdaterState.setMapDraggedDelta(event.getX(), event.getY());
  }

  @Subscribe
  public void OnUserChangeZoom(EventUserZoomChanged event) {
    switch (event.getZoomCommand()){
      case IncrementZoom:
        uiUpdaterState.incrementZoomLevel();
        break;
      case DecrementZoom:
        uiUpdaterState.decrementZoomLevel();
        break;
      case ZoomReset:
        uiUpdaterState.zoomReset();
        break;
    }
    Logger.debug("Received UserZoomChanged: " + uiUpdaterState.getZoomLevel());
  }

  // Get the cell selected in a certain range
  private int GetCellInRange(double distLength, int cellCount, double distPoint) {
    double cellWidth = distLength/cellCount;
    double cellsOver = distPoint / cellWidth;
    double cell = Math.floor(cellsOver);
    return (int) cell;
  }
}
