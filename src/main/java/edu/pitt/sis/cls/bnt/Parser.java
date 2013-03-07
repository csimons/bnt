package edu.pitt.sis.cls.bnt;

import edu.pitt.sis.cls.bnt.lang.Bnt;

import java.io.InputStream;

public interface Parser {
	public Bnt parse(InputStream is) throws Exception;
}
