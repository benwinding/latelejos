package RobotRemote.UIServices.MapTranslation;



import javax.xml.bind.JAXBException;

public interface IRobotMapTranslator {
  public String createXml(Lunarovermap mapTransferObject) throws JAXBException;
  public Lunarovermap createMapObject(String mapXml) throws JAXBException;
}
