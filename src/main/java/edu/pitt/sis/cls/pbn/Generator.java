package edu.pitt.sis.cls.pbn;

import edu.pitt.sis.cls.pbn.lang.Pbn;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Generator {
    private Map<String, Class<? extends Parser>> parsers;

    public Generator() {
        parsers = new HashMap<String, Class<? extends Parser>>();
        parsers.put("pbn", XMLParser.class);
    }

    public void run(String sourceFilename, String outputFilename)
            throws Exception {
        InputStream is = new FileInputStream(sourceFilename);
        Parser parser = parsers.get(extension(sourceFilename)).newInstance();
        Pbn pbn = parser.parse(is);
        is.close();
    }

    private String extension(String filename) {
        String[] segments = filename == null
            ? new String[] {""}
            : filename.split("\\.");

        return segments.length == 1
            ? ""
            : segments[segments.length - 1];
    }
}
