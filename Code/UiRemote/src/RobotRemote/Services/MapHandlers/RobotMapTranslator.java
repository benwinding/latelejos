package RobotRemote.Services.MapHandlers;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/*<!DOCTYPE lunarrovermap [  <!-- A Luna Rover map contianing one boundary, vehicle status, apollo landing site,
rover landing site and track to color mapping, with any number of obstacles, zones, tracks and additional attributes.
 The Luna Rover map uses a global unit of mesurement for all measurmenets -->

*/


public class RobotMapTranslator implements IRobotMapTranslator {
    public RobotMapTranslator(){

        return;
    }


    public String createXml(MapTransferObject mapTransferObject){

        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(MapTransferObject.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);

            
                jaxbMarshaller.marshal(mapTransferObject,System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return " ";
    }


    public MapTransferObject createMapObject(String mapXml){

    return null;
    }

}

