package RobotRemote.UIServices.MapHandlers;

import RobotRemote.Models.MapPoint;
import RobotRemote.RobotServices.Sensors.DiscoveredColoursState;
import RobotRemote.Shared.AppStateRepository;
import RobotRemote.Shared.Logger;
import RobotRemote.Shared.ServiceManager;
import RobotRemote.UIServices.Events.*;
import RobotRemote.Shared.RobotConfiguration;
import RobotRemote.UIServices.MapTranslation.MapTransferObject;
import RobotRemote.UIServices.MapTranslation.MapTranslator;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.Lunarovermap;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.XmlTranslator;
import RobotRemote.UIServices.UiUpdater.UiUpdaterState;
import com.google.common.eventbus.Subscribe;
import org.omg.IOP.Encoding;
import sun.nio.cs.UTF_32;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class MapInputEventHandlers {
  private final UserWaypointsState userWaypointsState;
  private final UserNoGoZoneState userNoGoZoneState;
  private final UiUpdaterState uiUpdaterState;
  private final ServiceManager sm;
  private final RobotConfiguration config;

  private HashMap<Integer, ArrayList<MapPoint>> colouredPoints;
  private AppStateRepository appStateRepository;
  private MapTransferObject mapObject;
  DiscoveredColoursState discoveredColoursState;

  public MapInputEventHandlers(ServiceManager sm) {
    sm.getEventBus().register(this);
    this.config = sm.getConfiguration();
    this.sm = sm;
    this.userNoGoZoneState = sm.getAppState().getUserNoGoZoneState();
    this.userWaypointsState = sm.getAppState().getUserWaypointsState();
    this.uiUpdaterState = sm.getAppState().getUiUpdaterState();
  }

  @Subscribe
  public void OnMapImport(EventMapImport event) {
    File importedMapFile = event.getSelectedMapFile();
    try {
      String xmlStringFromFile = this.readFile(importedMapFile.getAbsolutePath());
      MapTranslator mapTranslator = new MapTranslator();
      mapObject = mapTranslator.GetMapFromXmlString(xmlStringFromFile);
      if (mapObject!=null) Logger.log("import success");
      this.SetCurrentMap(mapObject);

    } catch (JAXBException e) {
      Logger.warn("Could not translate xml to map object");
    } catch (IOException e) {
      Logger.warn("Could read xml file: " + importedMapFile.getAbsolutePath());
    }
  }

  @Subscribe
  public void OnMapExport(EventMapExport event) {
    File exportMapFile = event.getSelectedExportMapFile();
    MapTransferObject currentMap = this.GetCurrentMap();
    try {
      String xmlMapString = new MapTranslator().GetXmlStringFromMap(currentMap);
      SaveFile(xmlMapString, exportMapFile);
    } catch (JAXBException e) {
      Logger.warn("Could not export map object to XML");
    }
  }

  private void SetCurrentMap(MapTransferObject mapObject) {
    // Set the mapTransfer object to the current state

    //locationstate, discoveredcolorstate, nogozonestate(implement later)
    this.sm.getAppState().getLocationState().SetCurrentLocation(mapObject.getCurrentPosition());
    //this.sm.getAppState().getLocationState().SetCurrentLocation(mapObject.getRoverLandingSite());
    //colourstate
    for (MapPoint testPoint : mapObject.getRadiation()){
      this.sm.getAppState().getDiscoveredColoursState().AddColouredPoint(0, testPoint);
    }
    for (MapPoint testPoint : mapObject.getNoGoZones()){
      this.sm.getAppState().getDiscoveredColoursState().AddColouredPoint(3, testPoint);
    }
    //test
  }
  private MapTransferObject GetCurrentMap() {
    // Get the current map state convert to the map transfer object


    return null;
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

  private String readFile(String path) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, Charset.forName("UTF-8"));
  }

  @Subscribe
  public void OnUserAddWaypoint(EventUserAddWaypoint event) {
    // Account for zoom on map
    float mapH = uiUpdaterState.getMapH();
    float mapW = uiUpdaterState.getMapW();
    float zoomLevel = uiUpdaterState.getZoomLevel();
    float pixelsPerCm = config.mapPixelsPerCm;

    // Mouse relative coordinates to scaled map
    double mouseXcm = event.getX()/pixelsPerCm;// - ((1-zoomLevel)/2)*mapW;
    double mouseYcm = event.getY()/pixelsPerCm;// config.mapPixelsPerCm - ((1-zoomLevel)/2)*mapH;

    // Translate mouse to actual map xy coordinates
    double transXcm = mouseXcm + mapW/2;// - ((1-zoomLevel)/2)*mapW;
    double transYcm = mouseYcm + mapH/2;// config.mapPixelsPerCm - ((1-zoomLevel)/2)*mapH;

    // Scale mouse to actual map xy coordinates
    double scaleX = transXcm / zoomLevel;
    double scaleY = transYcm / zoomLevel;
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
