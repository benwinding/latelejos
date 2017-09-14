package RobotRemote.Services.MapHandlers;


import javax.xml.bind.*;
import java.io.File;

public class RobotMapTranslator implements IRobotMapTranslator {
    public RobotMapTranslator(){

        return;
    }
    public String createXml(Lunarovermap mapTransferObject)throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance("RobotRemote.Services.MapHandlers");
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
        jaxbMarshaller.marshal(mapTransferObject,System.out);
        return " ";
    }


    public Lunarovermap createMapObject(String mapXml)throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance("RobotRemote.Services.MapHandlers");
        Unmarshaller unmarhsaller = jaxbContext.createUnmarshaller();
        Lunarovermap lunarmap = (Lunarovermap) unmarhsaller.unmarshal(new File(mapXml));
        return lunarmap;
    }

}

