package edu.pitt.sis.cls.bnt;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import edu.pitt.sis.cls.bnt.lang.CptSegment;
import edu.pitt.sis.cls.bnt.xdsl.Cpt;
import edu.pitt.sis.cls.bnt.xdsl.Extensions;
import edu.pitt.sis.cls.bnt.xdsl.Genie;
import edu.pitt.sis.cls.bnt.xdsl.Node;
import edu.pitt.sis.cls.bnt.xdsl.Nodes;
import edu.pitt.sis.cls.bnt.xdsl.Smile;
import edu.pitt.sis.cls.bnt.xdsl.State;

public class XDSLWriter implements Writer {
	private final Logger LOG;

	public XDSLWriter() {
		LOG = Logger.getLogger(this.getClass().getCanonicalName());
	}

	public String format(NodePool nodePool) {
		Smile smile = new Smile();
		smile.setVersion(new BigDecimal("1.0"));
		smile.setId("Network1");
		smile.setNumsamples(new BigInteger("1000"));
		smile.setDiscsamples(new BigInteger("10000"));
		Extensions extensions = new Extensions();
		smile.setExtensions(extensions);

		Nodes nodes = new Nodes();
		for (String key : nodePool.keySet()) {
			LOG.debug("Adding node [" + key +"].");
			nodes.getCpt().add(generateCpt(key, nodePool));
		}
		smile.setNodes(nodes);

		Genie genie = new Genie();
		extensions.setGenie(genie);
		genie.setVersion(new BigDecimal("1.0"));
		genie.setApp("GeNIe 2.0.4535.0");
		genie.setName("Network1");
		genie.setFaultnameformat("nodestate");
		for (String key : nodePool.keySet())
			genie.getNode().add(generateGenieNode(key, nodePool));

		StringWriter sw = new StringWriter();
		String result = null;
		try {
			JAXBContext jc = JAXBContext.newInstance("edu.pitt.sis.cls.bnt.xdsl");
			Marshaller m = jc.createMarshaller();
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
		cpt.setProbabilities(nodeInstance.cpt.size() == 0
				? nodeInstance.apriori
				: joinCPTSegments(nodeInstance));
		return cpt;
	}

	private String joinCPTSegments(NodeInstance nodeInstance) {
		List<String> pSegments = new LinkedList<String>();
		for (CptSegment cptSegment : nodeInstance.cpt)
			pSegments.add(cptSegment.getP());
		return join(pSegments, " ");
	}

	private Node generateGenieNode(String key, NodePool nodePool) {
		Node node = new Node();
		node.setId(key);
		node.setName(nodePool.get(key).name);
		return node;
	}

	private String join(List<String> items, String delimiter) {
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= items.size(); i += 1)
			sb.append(items.get(i - 1)).append(
					i == items.size() ? "" : delimiter);
		return sb.toString();
	}
}
