package RobotRemote.Services.MapHandlers;



import javax.xml.bind.JAXBException;

public interface IRobotMapTranslator {
  public String createXml(Lunarovermap mapTransferObject);
  public Lunarovermap createMapObject(String mapXml) throws JAXBException;
}
