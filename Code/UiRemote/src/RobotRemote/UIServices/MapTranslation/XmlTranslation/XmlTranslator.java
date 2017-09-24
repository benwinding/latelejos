package RobotRemote.UIServices.MapTranslation.XmlTranslation;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XmlTranslator implements IXmlTranslator {

    // Returns xml string, input is a Lunarrover object
    public String createXml(Lunarovermap lunarovermap) throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance("RobotRemote.UIServices.MapHandlers");
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
        // jaxbMarshaller.marshal(mapTransferObject);
        String outputXml = "";
        return outputXml;
    }

    // Returns Lunarrovermap object, input is a string of xml
    public Lunarovermap createMapObject(String inputXmlString) throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance("RobotRemote.UIServices.MapTranslation");
        Unmarshaller unmarhsaller = jaxbContext.createUnmarshaller();
        Lunarovermap lunarmap = null;//(Lunarovermap) unmarhsaller.unmarshal(new File(inputXmlFilePath));
        return lunarmap;
    }
}
