package RobotRemote.UIServices.MapTranslation;

import RobotRemote.Models.MapPoint;
import RobotRemote.RobotServices.Movement.LocationState;
import RobotRemote.Shared.AppStateRepository;
import RobotRemote.Shared.RobotConfiguration;
import RobotRemote.Shared.ServiceManager;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.Lunarovermap;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.ObjectFactory;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.XmlTranslator;
import javafx.scene.paint.Color;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import javax.xml.bind.JAXBException;
import java.util.*;

import static java.awt.Color.BLACK;

public class MapTranslator implements IMapTranslator{

  private Lunarovermap.Zone findZone(List<Lunarovermap.Zone> zonelist, String state) throws NotFound {
    for (Lunarovermap.Zone zonei : zonelist){
      String zoneState = zonei.getState();
      if (Objects.equals(zoneState, state)) {
        if(zonei.area!=null){
          return zonei;
        }
      }
    }
    throw new NotFound();
  }
  private Lunarovermap.Track findTrack(List<Lunarovermap.Track> tracklist, String type) throws NotFound {
    for (Lunarovermap.Track tracki : tracklist){
      String zoneState = tracki.getType();
      if (Objects.equals(zoneState, type)) {
        if(tracki.point!=null){
          return tracki;
        }
      }
    }
    throw new NotFound();
  }

  private void addZoneMapPoints( ArrayList<MapPoint> listName, Lunarovermap.Zone zoneName) {
    if (zoneName != null) {
      for (Lunarovermap.Zone.Area.Point genericPoint : zoneName.area.point) {
        int x = 0, y = 0;
        x = genericPoint.getX() / 10;
        y = genericPoint.getY() / 10;
        MapPoint point = new MapPoint(x, y);
        listName.add(point);
      }
    }
  }
  private void addTrackMapPoints( ArrayList<MapPoint> listName, Lunarovermap.Track trackName) {
    if (trackName != null) {
      for (Lunarovermap.Track.Point genericPoint : trackName.point) {
        int x = 0, y = 0;
        x = genericPoint.getX()/ 10;
        y = genericPoint.getY()/ 10;
        MapPoint point = new MapPoint(x, y);
        listName.add(point);
      }
    }
  }
    //input: xmlstring, outputs mapTransferObject
  @Override
  public MapTransferObject GetMapFromXmlString(String xmlString) throws JAXBException {
    //create lunarRoverMap object:
    Lunarovermap mapFromString = new XmlTranslator().createMapObject(xmlString);
    //Get list of generic zones from object
    List<Lunarovermap.Zone> zoneList = mapFromString.zone;
    //Get list of different track types
    List<Lunarovermap.Track> tracksList = mapFromString.track;

    //Convert LunarRoverMap to maptransferObject:
    //set colors from xml
    Color vehicleColor=null;
    Color footprintColor = null;
    Color landingColor=null;
    for (Lunarovermap.TrackToColor.Attribute track: mapFromString.trackToColor.attribute ){
      String trackName=track.key;
      String trackColorHex=track.value;
      if (trackName=="vehicle"){
        vehicleColor=Color.web(track.value);
      }else if (trackName=="footprint"){
        footprintColor=Color.web(track.value);
      }else if (trackName=="landing"){
        landingColor=Color.web(track.value);
      }
      }

    //current point:
    int currentX= mapFromString.vehicleStatus.point.getX()/ 10;
    int currentY=mapFromString.vehicleStatus.point.getY()/ 10;
    int currentTheta=mapFromString.vehicleStatus.attribute.getValue();
    MapPoint currentPos= new MapPoint(currentX,currentY, currentTheta);
    ////////////////////
    //rover landing site:
    int roverX=mapFromString.roverLandingSite.point.getX()/ 10;
    int roverY=mapFromString.roverLandingSite.point.getY()/ 10;
    MapPoint roverLandingSite= new MapPoint(roverX, roverY);
    /////////////////////
    //Obstacles
    ArrayList<MapPoint> obstacleList=new ArrayList<>();
    for (Lunarovermap.Obstacle.Point obstacle : mapFromString.obstacle.point){
      int x = 0, y = 0;
      x = obstacle.getX()/ 10;
      y = obstacle.getY()/ 10;
      MapPoint point = new MapPoint(x, y);
      obstacleList.add(point);
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
    addZoneMapPoints(noGo, noGoZone1);
    /////////////////////
    //Crater Zones
    ArrayList<MapPoint> craters = new ArrayList<>();
    Lunarovermap.Zone craterZone = null;
    try {
      craterZone = findZone(zoneList, "crater");
    } catch (NotFound notFound) {
      notFound.printStackTrace();
    }
    addZoneMapPoints(craters, craterZone);
    /////////////////////
    //Explored Zones
    ArrayList<MapPoint> explored= new ArrayList<>();
    Lunarovermap.Zone exploredZone = null;
    try {
      exploredZone = findZone(zoneList, "explored");
    } catch (NotFound notFound) {
      notFound.printStackTrace();
    }
    addZoneMapPoints(explored, exploredZone);
    /////////////////////
    //Unexplored Zones
    ArrayList<MapPoint> unexplored= new ArrayList<>();
    Lunarovermap.Zone unexploredZone = null;
    try {
      unexploredZone = findZone(zoneList, "unexplored");
    } catch (NotFound notFound) {
      notFound.printStackTrace();
    }
    addZoneMapPoints(unexplored, unexploredZone);

    /////////////////////
    //radiation zone
    ArrayList<MapPoint> radiation = new ArrayList<>();
    Lunarovermap.Zone radiationZone = null;
    try {
      radiationZone = findZone(zoneList, "radiation");
    } catch (NotFound notFound) {
      notFound.printStackTrace();
    }
    addZoneMapPoints(radiation, radiationZone);
    /////////////////////
    //Boundary
    ArrayList<MapPoint> boundary=new ArrayList<>();
    Lunarovermap.Boundary lunarBoundary=mapFromString.boundary;
    if (lunarBoundary!= null) {
      for (Lunarovermap.Boundary.Area.Point genericPoint : lunarBoundary.area.point) {
        int x = 0, y = 0;
        x = genericPoint.getX()/ 10;
        y = genericPoint.getY()/ 10;
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
    addTrackMapPoints(landingTracks, lunarmapLandingTracks);




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
    return mapTransferObject;
  }

  //inputs mapTransferObject, outputs xmlstring
  @Override
  public String GetXmlStringFromMap(MapTransferObject mapTransferObject) throws JAXBException {
    ObjectFactory objectFactory= new ObjectFactory();
    Lunarovermap lunarovermap= objectFactory.createLunarovermap();

    //Convert LunarRoverMap to maptransferObject:
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
    lunarovermap.roverLandingSite.point.setX((int) mapTransferObject.roverLandingSite.y);
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
      Lunarovermap.Zone.Area.Point point1= objectFactory.createLunarovermapZoneAreaPoint();
      int x=0, y=0;
      point1.x=(int) point.x;
      point1.y=(int) point.y;
      nogoZone.area.point.add(point1);
    }

    lunarovermap.zone.add(nogoZone);


  String mapFromString = new XmlTranslator().createXml(lunarovermap);
    return mapFromString;
  }
}
