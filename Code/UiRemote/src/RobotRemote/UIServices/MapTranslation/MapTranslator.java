package RobotRemote.UIServices.MapTranslation;

import RobotRemote.Models.MapPoint;
import RobotRemote.RobotServices.Movement.LocationState;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.Lunarovermap;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.XmlTranslator;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        x = genericPoint.getX();
        y = genericPoint.getY();
        MapPoint point = new MapPoint(x, y);
        listName.add(point);
      }
    }
  }
  private void addTrackMapPoints( ArrayList<MapPoint> listName, Lunarovermap.Track trackName) {
    if (trackName != null) {
      for (Lunarovermap.Track.Point genericPoint : trackName.point) {
        int x = 0, y = 0;
        x = genericPoint.getX();
        y = genericPoint.getY();
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
    //current point:
    int currentX= mapFromString.vehicleStatus.point.getX();
    int currentY=mapFromString.vehicleStatus.point.getY();
    int currentTheta=mapFromString.vehicleStatus.attribute.getValue();
    MapPoint currentPos= new MapPoint(currentX,currentY, currentTheta);
    ////////////////////
    //rover landing site:
    int roverX=mapFromString.roverLandingSite.point.getX();
    int roverY=mapFromString.roverLandingSite.point.getY();
    MapPoint roverLandingSite= new MapPoint(roverX, roverY);
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
    //radiation
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
        x = genericPoint.getX();
        y = genericPoint.getY();
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
    mapTransferObject.setCurrentPosition(currentPos);
    mapTransferObject.setRoverLandingSite(roverLandingSite);
    mapTransferObject.setNoGoZones(noGo);
    mapTransferObject.setCraters(craters);
    mapTransferObject.setRadiation(radiation);
    mapTransferObject.setBoundary(boundary);
    mapTransferObject.setLandingtracks(landingTracks);
    return mapTransferObject;
  }

  //inputs mapTransferObject, outputs xmlstring
  @Override
  public String GetXmlStringFromMap(MapTransferObject mapTransferObject) throws JAXBException {
    Lunarovermap lunarovermap = new Lunarovermap();
    // .....
    // Set all lunarrovermap properties from MapTransferObject
    // .....
    String mapFromString = new XmlTranslator().createXml(lunarovermap);
    return mapFromString;
  }
}
