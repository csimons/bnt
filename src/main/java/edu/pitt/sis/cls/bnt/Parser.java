package edu.pitt.sis.cls.bnt;

import java.io.InputStream;

import edu.pitt.sis.cls.bnt.lang.Bnt;

public interface Parser {
	public Bnt parse(InputStream is) throws Exception;
}
