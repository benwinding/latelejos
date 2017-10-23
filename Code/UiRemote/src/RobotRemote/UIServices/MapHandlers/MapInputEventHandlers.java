package RobotRemote.UIServices.MapHandlers;

import RobotRemote.Models.MapPoint;
import RobotRemote.RobotServices.Movement.LocationState;
import RobotRemote.RobotServices.Sensors.DiscoveredColoursState;
import RobotRemote.RobotStateMachine.Events.AutoSurvey.EventAutomapDetectedObject;
import RobotRemote.RobotStateMachine.Events.Shared.EventSwitchToAutoMap;
import RobotRemote.Shared.Logger;
import RobotRemote.Shared.RobotConfiguration;
import RobotRemote.Shared.ServiceManager;
import RobotRemote.UIServices.Events.*;
import RobotRemote.UIServices.MapTranslation.MapTransferObject;
import RobotRemote.UIServices.MapTranslation.MapTranslator;
import RobotRemote.UIServices.UiUpdater.UiUpdaterState;
import com.google.common.eventbus.Subscribe;
import lejos.robotics.navigation.Pose;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MapInputEventHandlers {
  private final UserWaypointsState userWaypointsState;
  private final UserNoGoZoneState userNoGoZoneState;
  private final UiUpdaterState uiUpdaterState;
  private final ServiceManager sm;
  private final RobotConfiguration config;

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
      MapTransferObject mapObject;
      mapObject = mapTranslator.GetMapFromXmlString(xmlStringFromFile);
      if (mapObject!=null) Logger.log("import success");
      this.SetCurrentMap(mapObject);

    } catch (JAXBException e) {
      Logger.warn("Could not translate xml to map object");
    } catch (IOException e) {
      Logger.warn("Could not read xml file: " + importedMapFile.getAbsolutePath());
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
    this.config.colorTrail = mapObject.getVehicleTrackColor();
    LocationState locationState = this.sm.getAppState().getLocationState();
    DiscoveredColoursState discoveredState = this.sm.getAppState().getDiscoveredColoursState();
    UserNoGoZoneState ngzState = this.sm.getAppState().getUserNoGoZoneState();

    locationState.SetExploredAreaPoints(mapObject.getExplored());
    locationState.SetCurrentLocation(mapObject.getCurrentPosition());
    discoveredState.AddColouredPoint(7,mapObject.getRoverLandingSite());

    if (mapObject.getApolloLandingSite()!=null) {
      discoveredState.AddColouredPoint(7,mapObject.getApolloLandingSite());
    }

    //colourstate
    for (MapPoint testPoint : mapObject.getRadiation()){
      discoveredState.AddColouredPoint(0, testPoint);
    }

    ngzState.AddNgzSet(mapObject.getNoGoZones());

    for (MapPoint testPoint : mapObject.getBoundary()){
      discoveredState.AddColouredPoint(2, testPoint);
    }
    for (MapPoint testPoint : mapObject.getLandingtracks()){
      discoveredState.AddColouredPoint(3, testPoint);
    }
    for (MapPoint testPoint : mapObject.getCraters()){
      discoveredState.AddColouredPoint(4, testPoint);
    }
    for (MapPoint testPoint : mapObject.getUnexplored()){
      discoveredState.AddColouredPoint(6, testPoint);
    }
    for (MapPoint testPoint : mapObject.getObstacles()){
      discoveredState.AddColouredPoint(7, testPoint);
    }
  }

  private MapTransferObject GetCurrentMap() {
    // Get the current map state convert to the map transfer object
    MapTransferObject mapToExport = new MapTransferObject();
    mapToExport.setVehicleTrackColor(this.config.colorTrail);

    // Ngz state
    UserNoGoZoneState ngzState = this.sm.getAppState().getUserNoGoZoneState();
    mapToExport.setNoGoZones(ngzState.GetNgzPointsFlattened());
    mapToExport.setObstacles(ngzState.GetObstacles());
    // Tracks state
    DiscoveredColoursState coloursState = this.sm.getAppState().getDiscoveredColoursState();
    mapToExport.setBoundary(coloursState.GetPointsMatching(config.colorBorder));
    mapToExport.setRadiation(coloursState.GetPointsMatching(config.colorRadiation));
    mapToExport.setLandingtracks(coloursState.GetPointsMatching(config.colorTrail));
    mapToExport.setVehicleTracks(coloursState.GetPointsMatching(config.colorTrail));
    mapToExport.setFootprintTracks(coloursState.GetPointsMatching(config.colorTrail));
    mapToExport.setCraters(coloursState.GetPointsMatching(config.colorCrater));
    // Location state
    LocationState locationState = this.sm.getAppState().getLocationState();
    // mapToExport.setUnexplored((ArrayList<MapPoint>) locationState.GetPointsVisited());
    mapToExport.setExplored((ArrayList<MapPoint>) locationState.GetExploredAreaPoints());
    mapToExport.setCurrentPosition(locationState.GetCurrentPosition());
    mapToExport.setRoverLandingSite(new MapPoint(config.initX,config.initY,config.initTheta));

    return mapToExport;
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
    MapPoint newPoint = this.ScaleMouseInputToMap(event.getX(), event.getY());
    // Translate mouse coordinates to account for map centering
    Logger.debug(String.format("Received UserAddWaypoint:: x:%.1f, y:%.1f", newPoint.x, newPoint.y));
    userWaypointsState.AddWayPoint(newPoint.x,newPoint.y);
    sm.getEventBus().post(new EventSwitchToAutoMap(newPoint));
  }

  @Subscribe
  public void OnEventAutomapDetectedObject(EventAutomapDetectedObject event) {
    Pose detectedLocation = event.getDetectedPosition();
    this.userWaypointsState.AddWayPoint(detectedLocation.getX(), detectedLocation.getY());
  }

  @Subscribe
  public void OnUserMapNgzStart(EventUserMapNgzStart event){
    MapPoint newPoint = this.ScaleMouseInputToMap(event.getX(), event.getY());
    userNoGoZoneState.AddNgzStartPoint(newPoint);
  }

  @Subscribe
  public void OnUserMapNgzMiddle(EventUserMapNgzMiddle event){
    MapPoint newPoint = this.ScaleMouseInputToMap(event.getX(), event.getY());
    userNoGoZoneState.AddNgzMiddlePoint(newPoint);
  }

  @Subscribe
  public void OnUserMapNgzEnd(EventUserMapNgzEnd event){
    userNoGoZoneState.FinishedNgzSet();
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

  public MapPoint ScaleMouseInputToMap(double mouseX, double mouseY) {
    float mapH = uiUpdaterState.getMapH();
    float mapW = uiUpdaterState.getMapW();
    float zoomLevel = uiUpdaterState.getZoomLevel();
    float pixelsPerCm = config.mapPixelsPerCm;

    // Scale mouse to original map pixel coordinates
    double scaleX = mouseX / zoomLevel;
    double scaleY = mouseY / zoomLevel;

    // Get cm coordinates from scaled coordinates
    double scaleXcm = scaleX / pixelsPerCm;
    double scaleYcm = scaleY / pixelsPerCm;

    // Translate mouse coordinates to account for map centering
    double transXcm = scaleXcm + mapW/2;
    double transYcm = scaleYcm + mapH/2;

    return new MapPoint(transXcm, transYcm);
  }
}
