package edu.pitt.sis.cls.bnt;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import edu.pitt.sis.cls.bnt.xdsl.Barchart;
import edu.pitt.sis.cls.bnt.xdsl.Cpt;
import edu.pitt.sis.cls.bnt.xdsl.Extensions;
import edu.pitt.sis.cls.bnt.xdsl.Font;
import edu.pitt.sis.cls.bnt.xdsl.Genie;
import edu.pitt.sis.cls.bnt.xdsl.Interior;
import edu.pitt.sis.cls.bnt.xdsl.Node;
import edu.pitt.sis.cls.bnt.xdsl.Nodes;
import edu.pitt.sis.cls.bnt.xdsl.Outline;
import edu.pitt.sis.cls.bnt.xdsl.Smile;
import edu.pitt.sis.cls.bnt.xdsl.State;

public class XDSLWriter implements Writer {
    /**
     * Absolute node location pixel specifications.
     */
    private static final int NODE_PADDING_PX = 20;
    private static final int width           = 240;
    private static final int height          = 40;
    private static final int nodesPerRow     = 3;

    private int top   = NODE_PADDING_PX;
    private int left  = NODE_PADDING_PX;
    private int count = 0;

    public String format(NodePool nodePool) {
        Smile smile = new Smile();
        smile.setVersion(new BigDecimal("1.0"));
        smile.setId("Network1");
        smile.setNumsamples(new BigInteger("1000"));
        smile.setDiscsamples(new BigInteger("10000"));
        Extensions extensions = new Extensions();
        smile.setExtensions(extensions);

        List<String> sortedInstanceIDs = nodePool.dependencySortedKeyList();

        Nodes nodes = new Nodes();
        for (String key : sortedInstanceIDs) {
            nodes.getCpt().add(generateCpt(key, nodePool));
        }
        smile.setNodes(nodes);

        Genie genie = new Genie();
        extensions.setGenie(genie);
        genie.setVersion(new BigDecimal("1.0"));
        genie.setApp("GeNIe 2.0.4535.0");
        genie.setName("Network1");
        genie.setFaultnameformat("nodestate");
        for (String key : sortedInstanceIDs)
            genie.getNode().add(generateGenieNode(key, nodePool));

        StringWriter sw = new StringWriter();
        String result = null;
        try {
            JAXBContext jc = JAXBContext.newInstance(
                    "edu.pitt.sis.cls.bnt.xdsl");
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(smile, sw);
            result = sw.toString();
        } catch (Exception e) {}
        return result;
    }

    private Cpt generateCpt(String key, NodePool nodePool) {
        NodeInstance nodeInstance = nodePool.get(key);
        Cpt cpt = new Cpt();
        cpt.setId(nodeInstance.id);
        String[] states = nodeInstance.states.split(" ");
        for (int i = 0; i < states.length; i += 1) {
            State state = new State();
            state.setId(states[i]);
            cpt.getState().add(state);
        }
        cpt.setParents(nodeInstance.parents);
        cpt.setProbabilities(nodeInstance.cpt != null
                ? nodeInstance.cpt
                : nodeInstance.apriori);
        return cpt;
    }

    private Node generateGenieNode(String key, NodePool nodePool) {
        Node node = new Node();
        node.setId(key);
        node.setName(nodePool.get(key).name);

        Interior interior = new Interior();
        interior.setColor("e5f6f7");
        node.setInterior(interior);
    
        Outline outline = new Outline();
        outline.setColor("000080");
        node.setOutline(outline);

        Font font = new Font();
        font.setName("Arial");
        font.setSize(new BigInteger("8"));
        font.setColor("000000");
        node.setFont(font);

        // Pixel order in string: Right edge, bottom, left edge, top.
        // node.setPosition("260 60 20 20"); // Stack all.

        // New "grid" rendering in lieu of a proper node-placement algorithm.
        count += 1;
        node.setPosition(String.format("%d %d %d %d",
                left + width, top + height, left, top));

        if (count % nodesPerRow == 0) {
            // Reset to new row.
            top += height + NODE_PADDING_PX;
            left = NODE_PADDING_PX;
        } else {
            left += width + NODE_PADDING_PX;
        }

        Barchart barchart = new Barchart();
        barchart.setActive(false);
        node.setBarchart(barchart);

        return node;
    }
}
