package edu.pitt.sis.cls.pbn;

import edu.pitt.sis.cls.pbn.lang.Pbn;

import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class XMLParser implements Parser {
    public Pbn parse(InputStream is) throws Exception {
        JAXBContext jc = JAXBContext.newInstance("edu.pitt.sis.cls.pbn.lang");
        Unmarshaller u = jc.createUnmarshaller();
        u.setValidating(true);
        Pbn pbn = (Pbn) u.unmarshal(is);
        return pbn;
    }
}
