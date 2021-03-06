package RobotRemote.UIServices.MapTranslation;

import RobotRemote.Models.MapPoint;
import RobotRemote.Shared.Logger;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.Lunarovermap;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.ObjectFactory;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.XmlTranslator;
import javafx.scene.paint.Color;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapTranslator implements IMapTranslator {

  private Lunarovermap.Zone findZone(List<Lunarovermap.Zone> zonelist, String state) throws NotFound {
    for (Lunarovermap.Zone zonei : zonelist) {
      String zoneState = zonei.getState();
      if (Objects.equals(zoneState, state)) {
        if (zonei.area != null) {
          return zonei;
        }
      }
    }
    throw new NotFound();
  }

  private Lunarovermap.Track findTrack(List<Lunarovermap.Track> tracklist, String type) throws NotFound {
    for (Lunarovermap.Track tracki : tracklist) {
      String zoneState = tracki.getType();
      if (Objects.equals(zoneState, type)) {
        if (tracki.point != null) {
          return tracki;
        }
      }
    }
    return new Lunarovermap.Track();
    //throw new NotFound();
  }

  private void addZoneMapPoints(ArrayList<MapPoint> listName, Lunarovermap.Zone zoneName, String units) {
    if (zoneName == null)
      return;
    if (zoneName.area == null)
      return;
    if (zoneName.area.point == null)
      return;

    for (Lunarovermap.Zone.Area.Point genericPoint : zoneName.area.point) {
      int x = 0, y = 0;
      x = genericPoint.getX() ;
      y = genericPoint.getY() ;
      if(Objects.equals(units, "metres")){
        x=x/10;
        y=y/10;
      }
      MapPoint point = new MapPoint(x, y);
      listName.add(point);
    }
  }

  private void addTrackMapPoints(ArrayList<MapPoint> listName, Lunarovermap.Track trackName, String units) {
    if (trackName != null && trackName.point != null) {
      for (Lunarovermap.Track.Point genericPoint : trackName.point) {
        int x = 0, y = 0;
        x = genericPoint.getX() ;
        y = genericPoint.getY() ;
        if(Objects.equals(units, "metres")){
          x=x/10;
          y=y/10;
        }
        MapPoint point = new MapPoint(x, y);
        listName.add(point);
      }
    }
  }

  public void validateImport(Lunarovermap mapFromString){
    if(mapFromString.trackToColor==null){
      Logger.warn(" Invalid track color settings detected");
    }
    if(mapFromString.roverLandingSite.point==null){
      Logger.warn(" invalid rover landing site");
    }
    if(mapFromString.vehicleStatus.point==null){
      Logger.warn("Invalid current position");
    }
    if(mapFromString.boundary.area.point==null){
      Logger.warn("Invalid boundary");
    }
  }
  //input: xmlstring, outputs mapTransferObject
  @Override
  public MapTransferObject GetMapFromXmlString(String xmlString) throws JAXBException {
    //create lunarRoverMap object:
    Lunarovermap mapFromString = new XmlTranslator().createMapObject(xmlString);
    //Get list of generic zones from object
    validateImport(mapFromString);

    List<Lunarovermap.Zone> zoneList = mapFromString.zone;
    //Get list of different track types
    List<Lunarovermap.Track> tracksList = mapFromString.track;

    //Convert LunarRoverMap to maptransferObject:
    //set colors from xml
    Color vehicleColor = null;
    Color footprintColor = null;
    Color landingColor = null;

    if (mapFromString.trackToColor!=null) {
      for (Lunarovermap.TrackToColor.Attribute track : mapFromString.trackToColor.attribute) {
        String trackName = track.key;
        String trackColorHex = track.value;
        if (trackName == "vehicle") {
          vehicleColor = Color.web(track.value);
        } else if (trackName == "footprint") {
          footprintColor = Color.web(track.value);
        } else if (trackName == "landing") {
          landingColor = Color.web(track.value);
        }
      }
    }

    //current point:
    int currentX = mapFromString.vehicleStatus.point.getX();
    int currentY = mapFromString.vehicleStatus.point.getY();
    if(Objects.equals(mapFromString.units, "metres")){
      currentX=currentX/10;
      currentY=currentY/10;
    }
    int currentTheta = mapFromString.vehicleStatus.attribute.getValue();
    MapPoint currentPos = new MapPoint(currentX, currentY, currentTheta);
    ////////////////////
    //rover landing site:
    int roverX = mapFromString.roverLandingSite.point.getX() ;
    int roverY = mapFromString.roverLandingSite.point.getY() ;
    if(Objects.equals(mapFromString.units, "metres")){
      roverX=roverX/10;
      roverY=roverY/10;
    }
    MapPoint roverLandingSite = new MapPoint(roverX, roverY);
    /////////////////////
    //Obstacles
    ArrayList<MapPoint> obstacleList = null;
    if (mapFromString.obstacle.point!=null) {
      obstacleList = new ArrayList<>();
      for (Lunarovermap.Obstacle.Point obstacle : mapFromString.obstacle.point) {
        int x = 0, y = 0;
        x = obstacle.getX() / 10;
        y = obstacle.getY() / 10;
        MapPoint point = new MapPoint(x, y);
        obstacleList.add(point);
      }
    }
    /////////////////////
    //No Go Zones
    ArrayList<MapPoint> noGo = new ArrayList<>();
    Lunarovermap.Zone noGoZone1 = null;
    try {
      noGoZone1 = findZone(zoneList, "nogo");
    } catch (NotFound notFound) {
      notFound.printStackTrace();
    }
    addZoneMapPoints(noGo, noGoZone1, mapFromString.units);
    /////////////////////
    //Crater Zones
    ArrayList<MapPoint> craters = new ArrayList<>();
    Lunarovermap.Zone craterZone = null;
    try {
      craterZone = findZone(zoneList, "crater");
    } catch (NotFound notFound) {
      notFound.printStackTrace();
    }
    addZoneMapPoints(craters, craterZone, mapFromString.units);
    /////////////////////
    //Explored Zones
    ArrayList<MapPoint> explored = new ArrayList<>();
    Lunarovermap.Zone exploredZone = null;
    try {
      exploredZone = findZone(zoneList, "explored");
    } catch (NotFound notFound) {
      notFound.printStackTrace();
    }
    addZoneMapPoints(explored, exploredZone, mapFromString.units);
    /////////////////////
    //Unexplored Zones
    ArrayList<MapPoint> unexplored = new ArrayList<>();
    Lunarovermap.Zone unexploredZone = null;
    try {
      unexploredZone = findZone(zoneList, "unexplored");
    } catch (NotFound notFound) {
      notFound.printStackTrace();
    }
    addZoneMapPoints(unexplored, unexploredZone, mapFromString.units);

    /////////////////////
    //radiation zone
    ArrayList<MapPoint> radiation = new ArrayList<>();
    Lunarovermap.Zone radiationZone = null;
    try {
      radiationZone = findZone(zoneList, "radiation");
    } catch (NotFound notFound) {
      notFound.printStackTrace();
    }
    addZoneMapPoints(radiation, radiationZone, mapFromString.units);
    /////////////////////
    //Boundary
    ArrayList<MapPoint> boundary = new ArrayList<>();
    Lunarovermap.Boundary lunarBoundary = mapFromString.boundary;
    if (lunarBoundary != null && lunarBoundary.area.point != null) {
      for (Lunarovermap.Boundary.Area.Point genericPoint : lunarBoundary.area.point) {
        int x = 0, y = 0;
        x = genericPoint.getX() ;
        y = genericPoint.getY() ;
        if(Objects.equals(mapFromString.units, "metres")){
          x=x/10;
          y=y/10;
        }
        MapPoint point = new MapPoint(x, y);
        boundary.add(point);
      }
    }
    /////////////////////
    //Landing Tracks
    ArrayList<MapPoint> landingTracks = new ArrayList<>();
    Lunarovermap.Track lunarmapLandingTracks = null;
    try {
      lunarmapLandingTracks = findTrack(tracksList, "landing");
    } catch (NotFound notFound) {
      notFound.printStackTrace();
    }
    addTrackMapPoints(landingTracks, lunarmapLandingTracks, mapFromString.units);

    /////////////////////
    //Vehicle Tracks
    ArrayList<MapPoint> vehicleTracks= new ArrayList<>();
    Lunarovermap.Track lunarmapVehicleTrack = null;
    try {
      lunarmapVehicleTrack = findTrack(tracksList, "vehicle");
    } catch (NotFound notFound) {
      notFound.printStackTrace();
    }
    addTrackMapPoints(vehicleTracks, lunarmapVehicleTrack, mapFromString.units);
    /////////////////////
    //Footprint Tracks
    ArrayList<MapPoint> footprintTrack= new ArrayList<>();
    Lunarovermap.Track lunarmapFootprintTrack = null;
    try {
      lunarmapFootprintTrack = findTrack(tracksList, "landing");
    } catch (NotFound notFound) {
      notFound.printStackTrace();
    }
    addTrackMapPoints(footprintTrack, lunarmapFootprintTrack, mapFromString.units);




    MapTransferObject mapTransferObject = new MapTransferObject();
    // .....
    // Set all mapTransfer properties from lunarObject
    // .....
    mapTransferObject.setFootprintTrackColor(footprintColor);
    mapTransferObject.setLandingTrackColor(landingColor);
    mapTransferObject.setVehicleTrackColor(vehicleColor);
    mapTransferObject.setCurrentPosition(currentPos);
    mapTransferObject.setRoverLandingSite(roverLandingSite);
    mapTransferObject.setObstacles(obstacleList);
    mapTransferObject.setNoGoZones(noGo);
    mapTransferObject.setCraters(craters);
    mapTransferObject.setExplored(explored);
    mapTransferObject.setUnexplored(unexplored);
    mapTransferObject.setRadiation(radiation);
    mapTransferObject.setBoundary(boundary);
    mapTransferObject.setLandingtracks(landingTracks);
    mapTransferObject.setVehicleTracks(vehicleTracks);
    mapTransferObject.setFootprintTracks(footprintTrack);

    return mapTransferObject;
  }

  public void setPoints(Lunarovermap.Zone zone, MapPoint point, ObjectFactory objectFactory) {
  Lunarovermap.Zone.Area.Point point1 = objectFactory.createLunarovermapZoneAreaPoint();
  int x = 0, y = 0;
  point1.x=(int)point.x;
  point1.y=(int)point.y;
  zone.area.point.add(point1);
}

  //inputs mapTransferObject, outputs xmlstring
  @Override
  public String GetXmlStringFromMap(MapTransferObject mapTransferObject) throws JAXBException {
    ObjectFactory objectFactory= new ObjectFactory();
    Lunarovermap lunarovermap= objectFactory.createLunarovermap();

    //Convert LunarRoverMap to maptransferObject:

    lunarovermap.setUnits("cm");
    //set colors from xml
    Color vehicleColor=mapTransferObject.vehicleTrackColor;
    Color footprintColor = null;
    Color landingColor=null;
    Lunarovermap.TrackToColor.Attribute vehicletrack=new Lunarovermap.TrackToColor.Attribute();
    vehicletrack.setKey("vehicle");
    String vehicleHex = "";
    if (vehicleColor!=null) {
      vehicleHex = String.format( "#%02X%02X%02X",(int)( vehicleColor.getRed() * 255 ),
              (int)( vehicleColor.getGreen() * 255 ),(int)( vehicleColor.getBlue() * 255 ) );
    }

    vehicletrack.setValue(vehicleHex);
    //current point:
    Lunarovermap.VehicleStatus vehicleStatus= objectFactory.createLunarovermapVehicleStatus();
    Lunarovermap.VehicleStatus.Attribute vehicleAttribute=objectFactory.createLunarovermapVehicleStatusAttribute();
    Lunarovermap.VehicleStatus.Point vehicleStatusPoint=objectFactory.createLunarovermapVehicleStatusPoint();
    lunarovermap.vehicleStatus=vehicleStatus;
    lunarovermap.vehicleStatus.attribute=vehicleAttribute;
    lunarovermap.vehicleStatus.point=vehicleStatusPoint;
    vehicleStatusPoint.setX((int) mapTransferObject.getCurrentPosition().x);
    vehicleStatusPoint.setY((int) mapTransferObject.getCurrentPosition().y);
    vehicleAttribute.setKey("rotation");
    vehicleAttribute.setValue((int) mapTransferObject.getCurrentPosition().theta);////////////////////
    //rover landing site:
    lunarovermap.roverLandingSite= objectFactory.createLunarovermapRoverLandingSite();
    lunarovermap.roverLandingSite.point =objectFactory.createLunarovermapRoverLandingSitePoint();
    lunarovermap.roverLandingSite.point.setX((int) mapTransferObject.roverLandingSite.x);
    lunarovermap.roverLandingSite.point.setY((int) mapTransferObject.roverLandingSite.y);
    /////////////////////
    //Obstacles
    lunarovermap.obstacle=objectFactory.createLunarovermapObstacle();
    lunarovermap.obstacle.point=new ArrayList<Lunarovermap.Obstacle.Point>();

    for (MapPoint point: mapTransferObject.getObstacles()){
      int x = 0, y = 0;
      Lunarovermap.Obstacle.Point point1=objectFactory.createLunarovermapObstaclePoint();
      point1.x=(int) point.x;
      point1.y=(int) point.y;
      lunarovermap.obstacle.point.add(point1);
    }
    /////////////////////
    //No Go Zones
    lunarovermap.zone=new ArrayList<Lunarovermap.Zone>();
    Lunarovermap.Zone nogoZone= objectFactory.createLunarovermapZone();
    nogoZone.area= objectFactory.createLunarovermapZoneArea();
    nogoZone.area.point=new ArrayList<Lunarovermap.Zone.Area.Point>();
    nogoZone.setState("nogo");
    for(MapPoint point : mapTransferObject.getNoGoZones()){
      setPoints(nogoZone, point, objectFactory);
    }
    lunarovermap.zone.add(nogoZone);
    /////////////////////
    //Crater Zones
    Lunarovermap.Zone craterZone=objectFactory.createLunarovermapZone();
    craterZone.area=objectFactory.createLunarovermapZoneArea();
    craterZone.area.point=new ArrayList<Lunarovermap.Zone.Area.Point>();
    craterZone.setState("crater");
    for(MapPoint point : mapTransferObject.getCraters()){
      setPoints(craterZone, point, objectFactory);
    }
    lunarovermap.zone.add(craterZone);
    ////////////////////
    //Explored Zone
    Lunarovermap.Zone exploredZone=objectFactory.createLunarovermapZone();
    exploredZone.area=objectFactory.createLunarovermapZoneArea();
    exploredZone.area.point=new ArrayList<Lunarovermap.Zone.Area.Point>();
    exploredZone.setState("explored");
    for(MapPoint point : mapTransferObject.getExplored()){
      setPoints(exploredZone, point, objectFactory);
    }
    lunarovermap.zone.add(exploredZone);
    //////////////////////
    //Unexplored
    Lunarovermap.Zone unexploredZone=objectFactory.createLunarovermapZone();
    unexploredZone.area=objectFactory.createLunarovermapZoneArea();
    unexploredZone.area.point=new ArrayList<Lunarovermap.Zone.Area.Point>();
    unexploredZone.setState("unexplored");
    for(MapPoint point : mapTransferObject.getUnexplored()){
      setPoints(unexploredZone, point, objectFactory);
    }
    lunarovermap.zone.add(unexploredZone);
    ////////////////////
    //Radiation Zone
    Lunarovermap.Zone radiationZone=objectFactory.createLunarovermapZone();
    radiationZone.area=objectFactory.createLunarovermapZoneArea();
    radiationZone.area.point=new ArrayList<Lunarovermap.Zone.Area.Point>();
    radiationZone.setState("radiation");
    for(MapPoint point : mapTransferObject.getRadiation()){
      setPoints(radiationZone, point, objectFactory);
    }
    lunarovermap.zone.add(radiationZone);
    ////////////////////
    //Boundary

    Lunarovermap.Boundary boundary =objectFactory.createLunarovermapBoundary();
    boundary.area= objectFactory.createLunarovermapBoundaryArea();
    boundary.area.point = new ArrayList<Lunarovermap.Boundary.Area.Point>();
    for (MapPoint point : mapTransferObject.getBoundary()){
      int x = 0, y = 0;
      Lunarovermap.Boundary.Area.Point point1=objectFactory.createLunarovermapBoundaryAreaPoint();
      point1.x=(int) point.x;
      point1.y=(int) point.y;
      boundary.area.point.add(point1);
    }
    lunarovermap.boundary=boundary;



    ///////////////////
    //Landing Tracks
    lunarovermap.track = new ArrayList<Lunarovermap.Track>();
    Lunarovermap.Track landingTrack=objectFactory.createLunarovermapTrack();
    landingTrack.point = new ArrayList<Lunarovermap.Track.Point>();
    landingTrack.type="landing";
    for (MapPoint point : mapTransferObject.getLandingtracks()){
      int x = 0, y = 0;
      Lunarovermap.Track.Point point1=objectFactory.createLunarovermapTrackPoint();
      point1.x=(int) point.x;
      point1.y=(int) point.y;
      landingTrack.point.add(point1);
    }
    lunarovermap.track.add(landingTrack);
    //////////////////
    //Vehicle Tracks
    Lunarovermap.Track vehicleTrack=objectFactory.createLunarovermapTrack();
    vehicleTrack.point = new ArrayList<Lunarovermap.Track.Point>();
    vehicleTrack.type="vehicle";
    for (MapPoint point : mapTransferObject.getVehicleTracks()){
      int x = 0, y = 0;
      Lunarovermap.Track.Point point1=objectFactory.createLunarovermapTrackPoint();
      point1.x=(int) point.x;
      point1.y=(int) point.y;
      vehicleTrack.point.add(point1);
    }
    lunarovermap.track.add(vehicleTrack);
    ///////////////////
    //footprint Tracks
    Lunarovermap.Track footprintTrack=objectFactory.createLunarovermapTrack();
    footprintTrack.point = new ArrayList<Lunarovermap.Track.Point>();
    footprintTrack.type="footprint";
    for (MapPoint point : mapTransferObject.getFootprintTracks()){
      int x = 0, y = 0;
      Lunarovermap.Track.Point point1=objectFactory.createLunarovermapTrackPoint();
      point1.x=(int) point.x;
      point1.y=(int) point.y;
      footprintTrack.point.add(point1);
    }
    lunarovermap.track.add(footprintTrack);






  String mapFromString = new XmlTranslator().createXml(lunarovermap);
    return mapFromString;
  }
}
