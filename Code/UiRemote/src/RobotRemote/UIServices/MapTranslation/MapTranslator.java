package RobotRemote.UIServices.MapTranslation;

import RobotRemote.RobotServices.Movement.LocationState;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.Lunarovermap;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.XmlTranslator;

import javax.xml.bind.JAXBException;

public class MapTranslator implements IMapTranslator{
  @Override
  public MapTransferObject GetMapFromXmlString(String xmlString) throws JAXBException {
    Lunarovermap mapFromString = new XmlTranslator().createMapObject(xmlString);
    LocationState newLocation = new LocationState(0,0,0);
    MapTransferObject mapTransferObject = null;//new MapTransferObject();
    // .....
    // Set all mapTransfer properties from lunarObject
    // .....
    return mapTransferObject;
  }

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
