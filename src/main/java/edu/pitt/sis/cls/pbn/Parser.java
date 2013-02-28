package edu.pitt.sis.cls.pbn;

import edu.pitt.sis.cls.pbn.lang.Pbn;

import java.io.InputStream;

public interface Parser {
    public Pbn parse(InputStream is) throws Exception;
}
