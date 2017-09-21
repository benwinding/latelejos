package RobotRemote.UIServices.MapHandlers;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class RobotMapTranslator implements IRobotMapTranslator {
    public RobotMapTranslator(){

        return;
    }
    public String createXml(Lunarovermap mapTransferObject)throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance("RobotRemote.UIServices.MapHandlers");
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);

        jaxbMarshaller.marshal(mapTransferObject,System.out);
        return " ";
    }


    public Lunarovermap createMapObject(String mapXml)throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance("RobotRemote.UIServices.MapHandlers");
        Unmarshaller unmarhsaller = jaxbContext.createUnmarshaller();
        Lunarovermap lunarmap = (Lunarovermap) unmarhsaller.unmarshal(new File(mapXml));
        return lunarmap;
    }

}

