package edu.pitt.sis.cls.pbn;

import edu.pitt.sis.cls.pbn.lang.Pbn;

import java.io.FileInputStream;
import java.io.InputStream;

public class Generator {
    public void run(String sourceFilename, String outputFilename)
            throws Exception {
        InputStream is = new FileInputStream(sourceFilename);
        Pbn pbn = new XMLParser().parse(is);
        is.close();
    }
}
