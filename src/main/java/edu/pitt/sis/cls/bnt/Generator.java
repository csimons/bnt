package edu.pitt.sis.cls.bnt;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.pitt.sis.cls.bnt.lang.Binding;
import edu.pitt.sis.cls.bnt.lang.Bnt;
import edu.pitt.sis.cls.bnt.lang.Node;
import edu.pitt.sis.cls.bnt.lang.Slice;

public class Generator {
    private Map<String, Class<? extends Parser>> parsers;

    public Generator() {
        parsers = new HashMap<String, Class<? extends Parser>>();
        parsers.put(Constants.BNT_EXTENSION, XMLParser.class);
    }

    public void run(String sourceFilename, String outputFilename)
            throws Exception {
        InputStream is = new FileInputStream(sourceFilename);
        Parser parser = parsers.get(extension(sourceFilename)).newInstance();
        Bnt bnt = parser.parse(is);
        is.close();

        List<List<NodeInstance>> domainLayers = new LinkedList<List<NodeInstance>>();
        for (Slice slice : bnt.getDomain().getSlice()) {
            List<NodeInstance> domainLayer = new LinkedList<NodeInstance>();
            for (Node templateNode : bnt.getTemplate().getNode()) {
                NodeInstance nodeInstance = new NodeInstance();
                Binding templateBinding = templateNode.getBinding();
                for (int i = 0; i < slice.getBinding().size(); i += 1) {
                    Binding domainBinding = slice.getBinding().get(i);
                    if (!domainBinding.getSymbol().equals(templateBinding.getSymbol()))
                        continue;
                    else {
                        nodeInstance.name = String.format("%s::%s[d=%d]",
                            domainBinding.getValue(),
                            templateNode.getName(), i);
                        domainLayer.add(nodeInstance);
                    }
                }
            }
            domainLayers.add(domainLayer);
        }
        // TODO: Collapse domain layers.
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
