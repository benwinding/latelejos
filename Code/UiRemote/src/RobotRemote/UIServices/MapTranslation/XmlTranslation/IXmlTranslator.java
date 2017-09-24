package RobotRemote.UIServices.MapTranslation.XmlTranslation;

import javax.xml.bind.JAXBException;

public interface IXmlTranslator {
  public String createXml(Lunarovermap lunarovermapObject) throws JAXBException;
  public Lunarovermap createMapObject(String mapXml) throws JAXBException;
}
