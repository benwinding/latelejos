package RobotRemote.Test;

import RobotRemote.Models.MapPoint;
import RobotRemote.UIServices.MapTranslation.MapTransferObject;
import RobotRemote.UIServices.MapTranslation.MapTranslator;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.Lunarovermap;
import RobotRemote.UIServices.MapTranslation.XmlTranslation.XmlTranslator;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MapTranslationTest {
  private String readFile(String path) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, Charset.forName("UTF-8"));
  }
  @ Test
  public void TestStringToLunarMap() throws IOException, JAXBException {
    String xmlstring = readFile("UiRemote/src/RobotRemote/Test/samplexml.xml");
    Lunarovermap mapFromString = new XmlTranslator().createMapObject(xmlstring);
    System.out.println("success");
  }

  @Test
  public void TestMapImport() throws IOException {
    //test map xml stuff
    Lunarovermap map1 = new Lunarovermap();
    XmlTranslator translator = new XmlTranslator();
    String xmlstring = readFile("UiRemote/src/RobotRemote/Test/samplexml.xml");
    try {
      map1=translator.createMapObject(xmlstring);
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

  @Test
  public void TestMapTranslator() throws IOException, JAXBException {
    String xmlPath= "UiRemote/src/RobotRemote/Test/samplexml.xml";
    String xmlstring = readFile(xmlPath);
    MapTranslator mapTranslator = new MapTranslator();
    MapTransferObject newMap = mapTranslator.GetMapFromXmlString(xmlstring);
    MapPoint testPoint=newMap.getCurrentPosition();
    ArrayList<MapPoint> nogolist=newMap.getNoGoZones();
    //System.out.println("xml path =" + xmlPath);
    System.out.println("currentPos X= "+ testPoint.x+ "Y= "+testPoint.y+ "theta= "+testPoint.theta);
    System.out.println("expected X= "+700+" Y"+100+ "Theta"+ 180);
    //nogo test
    System.out.println("NOGO TEST:");
    for ( MapPoint genericpoint : nogolist){
      printPoints(genericpoint);
    }
    //LANDINGTRACKS TEST
    System.out.println("LANDING TRACKS TEST:");
    for ( MapPoint genericpoint : newMap.getLandingtracks()){
      printPoints(genericpoint);
    }
    //Boundary Test
    System.out.println("BOUNDARY TEST:");
    for ( MapPoint genericpoint : newMap.getBoundary()){
      printPoints(genericpoint);
    }
  }
  private void printPoints(MapPoint genericpoint){
    int i=0;
    System.out.println("point" +i+" x ="+ genericpoint.x + " y = "+ genericpoint.y);
    i++;
  }


}