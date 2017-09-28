package RobotRemote.UIServices.MapTranslation;

import javax.xml.bind.JAXBException;

public interface IMapTranslator {
  MapTransferObject GetMapFromXmlString(String xmlString) throws JAXBException;
  String GetXmlStringFromMap(MapTransferObject mapTransferObject) throws JAXBException;
}
