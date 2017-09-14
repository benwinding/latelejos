package RobotRemote.Services.MapHandlers;


import javax.xml.bind.*;
import java.io.File;

/*<!DOCTYPE lunarrovermap [  <!-- A Luna Rover map contianing one boundary, vehicle status, apollo landing site,
rover landing site and track to color mapping, with any number of obstacles, zones, tracks and additional attributes.
 The Luna Rover map uses a global unit of mesurement for all measurmenets -->

*/


public class RobotMapTranslator implements IRobotMapTranslator {
    public RobotMapTranslator(){

        return;
    }


    public String createXml(Lunarovermap mapTransferObject){

        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance("RobotRemote.Services.MapHandlers");
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
            
                jaxbMarshaller.marshal(mapTransferObject,System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return " ";
    }


    public Lunarovermap createMapObject(String mapXml)throws JAXBException{

    JAXBContext jaxbContext = JAXBContext.newInstance("RobotRemote.Services.MapHandlers");

    Unmarshaller unmarhsaller = jaxbContext.createUnmarshaller();

        Lunarovermap lunarmap = (Lunarovermap) unmarhsaller.unmarshal(new File(mapXml));
    return lunarmap;
    }

}

