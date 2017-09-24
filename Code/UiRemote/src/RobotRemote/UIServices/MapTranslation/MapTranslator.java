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

    for (Lunarovermap.Zone zone : zonelist){
      String zoneState = zone.getState();
      if (Objects.equals(zoneState, state)){
        return zone;
      }
    }
    throw new NotFound();
  }



  //input: xmlstring, outputs mapTransferObject
  @Override
  public MapTransferObject GetMapFromXmlString(String xmlString) throws JAXBException {

    //create lunarRoverMap object:
    Lunarovermap mapFromString = new XmlTranslator().createMapObject(xmlString);
    //Get list of generic zones from object
    List<Lunarovermap.Zone> zoneList = mapFromString.zone;

    //Convert LunarRoverMap to maptransferObject:
    //current point:
    int currentX= mapFromString.vehicleStatus.point.getX();
    int currentY=mapFromString.vehicleStatus.point.getY();
    int currentTheta=mapFromString.vehicleStatus.attribute.getValue();
    MapPoint currentPos= new MapPoint(currentX,currentY, currentTheta);
    //rover landing site:
    int roverX=mapFromString.roverLandingSite.point.getX();
    int roverY=mapFromString.roverLandingSite.point.getY();
    MapPoint roverLandingSite= new MapPoint(roverX, roverY);
    //No Go Zones
    Lunarovermap.Zone noGoZone1=new Lunarovermap.Zone();
    ArrayList<MapPoint> noGo = new ArrayList<>();
    try {
      noGoZone1 = findZone(zoneList, "nogo");
    } catch (NotFound notFound) {
      notFound.printStackTrace();
    }
    List<Lunarovermap.Zone.Area.Point> noGoList;
    noGoList = noGoZone1.area.getPoint();
    for (Lunarovermap.Zone.Area.Point noGoPoint : noGoList){
        int x=0,y=0;
        x=noGoPoint.getX();
        y=noGoPoint.getY();
        MapPoint point = new MapPoint(x,y);
        noGo.add(point);
      }


    MapTransferObject mapTransferObject = new MapTransferObject();
    // .....
    // Set all mapTransfer properties from lunarObject
    // .....
    mapTransferObject.setCurrentPosition(currentPos);
    mapTransferObject.setRoverLandingSite(roverLandingSite);
    mapTransferObject.setNoGoZones(noGo);
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
