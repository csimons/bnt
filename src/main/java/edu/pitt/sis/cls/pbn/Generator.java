package edu.pitt.sis.cls.pbn;

import edu.pitt.sis.cls.pbn.lang.Pbn;

import java.io.FileInputStream;
import java.io.InputStream;

public class Generator {
    private static final String SAMPLE_PBN = "sample.pbn";

    public void run(String sourceFilename, String outputFilename)
            throws Exception {
        InputStream is;
        if (sourceFilename != null)
            is = new FileInputStream(sourceFilename);
        else
            is = this.getClass().getResourceAsStream(SAMPLE_PBN);

        Pbn pbn = new XMLParser().parse(is);
    }
}
