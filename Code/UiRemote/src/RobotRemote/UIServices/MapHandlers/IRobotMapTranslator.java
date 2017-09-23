package RobotRemote.UIServices.MapHandlers;

public interface IRobotMapTranslator {
  String createXml(MapTransferObject mapTransferObject);
  MapTransferObject createMapObject(String mapXml);
}
