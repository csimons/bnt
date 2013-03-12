package edu.pitt.sis.cls.bnt;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import edu.pitt.sis.cls.bnt.lang.Bnt;

public class XMLParser implements Parser {
	public Bnt parse(InputStream is) throws Exception {
		JAXBContext jc = JAXBContext.newInstance("edu.pitt.sis.cls.bnt.lang");
		Unmarshaller u = jc.createUnmarshaller();
		Bnt bnt = (Bnt) u.unmarshal(is);
		return bnt;
	}
}
