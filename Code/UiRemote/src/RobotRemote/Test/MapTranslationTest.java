package RobotRemote.Test;

import RobotRemote.UIServices.MapTranslation.XmlTranslation.Lunarovermap;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.XmlTranslator;
import org.junit.Test;

import javax.xml.bind.JAXBException;

public class MapTranslationTest {
  @Test
  public void TestMapImport() {
    //test map xml stuff
    Lunarovermap map1 = new Lunarovermap();
    XmlTranslator translator = new XmlTranslator();
    try {
      map1=translator.createMapObject("UiRemote/src/RobotRemote/Test/samplexml.xml");
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    System.out.println("Rover Landing point : " + map1.roverLandingSite.getPoint().getX() + " , " + map1.roverLandingSite.getPoint().getY());
    map1.roverLandingSite.point.setX(100);
    map1.roverLandingSite.point.setY(100);
    System.out.println("New Rover Landing point : " + map1.roverLandingSite.getPoint().getX() + " , " + map1.roverLandingSite.getPoint().getY());
    try {
      translator.createXml(map1);
    } catch (JAXBException e) {
      e.printStackTrace();
    }
  }
}
