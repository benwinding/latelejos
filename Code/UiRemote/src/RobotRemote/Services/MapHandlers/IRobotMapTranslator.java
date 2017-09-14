package RobotRemote.Services.MapHandlers;

public interface IRobotMapTranslator {
  public String createXml(MapTransferObject mapTransferObject);
  public MapTransferObject createMapObject(String mapXml);
}
