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
import RobotRemote.UIServices.UiUpdater.UiUpdaterState;
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
  private final UiUpdaterState updaterState;

  public MapExportHandlers (ServiceManager sm) {
    sm.getEventBus().register(this);
    this.config = sm.getConfiguration();
    this.sm = sm;
    this.locationState = this.sm.getAppState().getLocationState();
    this.discoveredState = this.sm.getAppState().getDiscoveredColoursState();
    this.ngzState = this.sm.getAppState().getUserNoGoZoneState();
    this.updaterState = this.sm.getAppState().getUiUpdaterState();
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
    /*
    case 0: return "RED";
    case 1: return "GREEN";
    case 2: return "BLUE";
    case 3: return "YELLOW";
    case 4: return "MAGENTA";
    case 5: return "ORANGE";
    case 6: return "WHITE";
    case 7: return "BLACK";
    case 8: return "PINK";
    case 9: return "GRAY";
    case 10: return "PURPLE";
    default: return "WHITE";
    */
    // Set the mapTransfer object to the current state
    this.config.colorTrail = mapObject.getVehicleTrackColor();
    //EXPLORED
    locationState.SetExploredAreaPoints(mapObject.getExplored());
    //CURRENT LOC
    locationState.SetCurrentLocation(mapObject.getCurrentPosition());
    //ROVER LANDING SITE
    discoveredState.AddColouredPoint(3,mapObject.getRoverLandingSite());
    //APOLLO SITE
        if (mapObject.getApolloLandingSite()!=null) {
      ngzState.AddDetectedAppollo((float)mapObject.getApolloLandingSite().x, (float)mapObject.getApolloLandingSite().y);
      discoveredState.AddColouredPoint(5,mapObject.getApolloLandingSite()); //0==RED
    }
    //RADIATION ZONE
    for (MapPoint testPoint : mapObject.getRadiation()){ //1==green
      discoveredState.AddColouredPoint(1, testPoint);
    }
    //NO GO ZONES
    ngzState.AddNgzSet(mapObject.getNoGoZones());
    //BORDER
    updaterState.SetBorderPoints(mapObject.getBoundary());
    //LANDING TRACKS
    for (MapPoint testPoint : mapObject.getLandingtracks()){
      discoveredState.AddColouredPoint(0, testPoint); //0==RED
    }
    //CRATERS (treat as NGZ)
    ngzState.AddNgzSet(mapObject.getCraters());
    for (MapPoint testPoint : mapObject.getCraters()){
      //discoveredState.AddColouredPoint(4, testPoint); //4==MAGENTA
    }
    //UNEXPLORED
    for (MapPoint testPoint : mapObject.getUnexplored()){ //6==WHITE
      discoveredState.AddColouredPoint(6, testPoint);
    }
    //OBSTACLE (treat as NGZ)
    for (MapPoint testPoint : mapObject.getObstacles()){ //7==BLACK
      ngzState.AddDetectedObstacle((float) testPoint.x, (float) testPoint.y);
    //discoveredState.AddColouredPoint(7, testPoint);
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
