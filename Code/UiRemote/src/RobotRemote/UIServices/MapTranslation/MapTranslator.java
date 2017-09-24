package RobotRemote.UIServices.MapTranslation;

import RobotRemote.Models.MapPoint;
import RobotRemote.RobotServices.Movement.LocationState;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.Lunarovermap;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.XmlTranslator;

import javax.xml.bind.JAXBException;

public class MapTranslator implements IMapTranslator{
  //input: xmlstring, outputs mapTransferObject
  @Override
  public MapTransferObject GetMapFromXmlString(String xmlString) throws JAXBException {
    //create lunarRoverMap object:
    Lunarovermap mapFromString = new XmlTranslator().createMapObject(xmlString);
    //Convert LunarRoverMap to maptransferObject:

    //test current point:
    MapPoint current =new MapPoint(666,666,10);
    MapTransferObject mapTransferObject = null;//new MapTransferObject();
    // .....
    // Set all mapTransfer properties from lunarObject
    // .....
    mapTransferObject.setCurrentPosition(current);
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
