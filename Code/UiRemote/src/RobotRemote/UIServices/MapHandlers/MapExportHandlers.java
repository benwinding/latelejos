package RobotRemote.UIServices.MapHandlers;

import RobotRemote.Models.MapPoint;
import RobotRemote.RobotServices.Movement.LocationState;
import RobotRemote.RobotServices.Sensors.DiscoveredColoursState;
import RobotRemote.Shared.Logger;
import RobotRemote.Shared.RobotConfiguration;
import RobotRemote.Shared.ServiceManager;
import RobotRemote.UIServices.Events.EventMapExport;
import RobotRemote.UIServices.Events.EventMapImport;
import RobotRemote.UIServices.MapTranslation.MapTransferObject;
import RobotRemote.UIServices.MapTranslation.MapTranslator;
import com.google.common.eventbus.Subscribe;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MapExportHandlers {
  private final RobotConfiguration config;
  private final ServiceManager sm;
  private final LocationState locationState;
  private final DiscoveredColoursState discoveredState;
  private final UserNoGoZoneState ngzState;

  public MapExportHandlers (ServiceManager sm) {
    sm.getEventBus().register(this);
    this.config = sm.getConfiguration();
    this.sm = sm;
    this.locationState = this.sm.getAppState().getLocationState();
    this.discoveredState = this.sm.getAppState().getDiscoveredColoursState();
    this.ngzState = this.sm.getAppState().getUserNoGoZoneState();
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

    locationState.SetExploredAreaPoints(mapObject.getExplored());
    locationState.SetCurrentLocation(mapObject.getCurrentPosition());
    discoveredState.AddColouredPoint(7,mapObject.getRoverLandingSite());

    if (mapObject.getApolloLandingSite()!=null) {
      discoveredState.AddColouredPoint(7,mapObject.getApolloLandingSite());
    }

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
    mapToExport.setNoGoZones(ngzState.GetNgzPointsFlattened());
    mapToExport.setObstacles(ngzState.GetObstacles());
    // Tracks state
    mapToExport.setBoundary(discoveredState.GetPointsMatching(config.colorBorder));
    mapToExport.setRadiation(discoveredState.GetPointsMatching(config.colorRadiation));
    mapToExport.setLandingtracks(discoveredState.GetPointsMatching(config.colorTrail));
    mapToExport.setVehicleTracks(discoveredState.GetPointsMatching(config.colorTrail));
    mapToExport.setFootprintTracks(discoveredState.GetPointsMatching(config.colorTrail));
    mapToExport.setCraters(discoveredState.GetPointsMatching(config.colorCrater));
    // Location state
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
}
