package RobotRemote.UIServices.MapTranslation.XmlTranslation;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlTranslator implements IXmlTranslator {

    // Returns xml string, input is a Lunarrover object
    public String createXml(Lunarovermap lunarovermap) throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance("RobotRemote.UIServices.MapTranslation.XmlTranslation");
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
        java.io.StringWriter xmlSw = new StringWriter();
        jaxbMarshaller.marshal(lunarovermap, xmlSw);
        String outputXmlstring = xmlSw.toString();
        return outputXmlstring;
    }

    // Returns Lunarrovermap object, input is a string of xml
    public Lunarovermap createMapObject(String inputXmlString) throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance("RobotRemote.UIServices.MapTranslation.XmlTranslation");
        Unmarshaller unmarhsaller = jaxbContext.createUnmarshaller();
        Lunarovermap lunarmap = null;
        StringBuffer xmlStr = new StringBuffer(inputXmlString);
        lunarmap = (Lunarovermap) unmarhsaller.unmarshal( new StreamSource( new StringReader( xmlStr.toString() ) ) );

        return lunarmap;
    }
}
