package edu.pitt.sis.cls.bnt;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.pitt.sis.cls.bnt.lang.Bnt;
import edu.pitt.sis.cls.bnt.lang.DomainObject;
import edu.pitt.sis.cls.bnt.lang.Node;
import edu.pitt.sis.cls.bnt.lang.Slice;

public class Generator {
	private Map<String, Class<? extends Parser>> parsers;

	public Generator() {
		parsers = new HashMap<String, Class<? extends Parser>>();
		parsers.put(Constants.BNT_EXTENSION, XMLParser.class);
		parsers.put(Constants.XML_EXTENSION, XMLParser.class);
	}

	private String extension(String filename) {
		String[] segments = filename == null
			? new String[] {""}
			: filename.split("\\.");

		return segments.length == 1
			? ""
			: segments[segments.length - 1];
	}

	public Map<String, NodeInstance> constructPoolFromFile(
			String sourceFilename) throws Exception {
		InputStream is = new FileInputStream(sourceFilename);
		Parser parser = parsers.get(extension(sourceFilename)).newInstance();
		Bnt bnt = parser.parse(is);
		is.close();
		return constructPool(bnt);
	}

	public Map<String, NodeInstance> constructPool(Bnt bnt) {
		return collapseDomainLayers(generateDomainLayers(bnt));
	}

	private List<List<NodeInstance>> generateDomainLayers(Bnt bnt) {
		List<List<NodeInstance>> domainLayers = new LinkedList<List<NodeInstance>>();
		for (Slice slice : bnt.getDomain().getSlice()) {
			List<NodeInstance> domainLayer = new LinkedList<NodeInstance>();
			for (DomainObject domainObj : slice.getDomainObject()) {
				for (Node templateNode : bnt.getTemplate().getNode()) {
					if (!templateNode.getBinding().equals(domainObj))
						continue;
					else {
						NodeInstance nodeInstance = new NodeInstance();
						nodeInstance.id = domainObj.getName();
						nodeInstance.cpt = templateNode.getCptSegment();
						nodeInstance.apriori = templateNode.getApriori();
						nodeInstance.templateNodeName = templateNode.getName();
						nodeInstance.domainSlice = slice.getId();
						nodeInstance.name = String.format("%s::%s[d=%d]",
								nodeInstance.id,
								nodeInstance.templateNodeName,
								nodeInstance.domainSlice);
						domainLayer.add(nodeInstance);
					}
				}
			}
		}
		return domainLayers;
	}

	private Map<String, NodeInstance> collapseDomainLayers(
			List<List<NodeInstance>> domainLayers) {
		Map<String, NodeInstance> nodePool = new HashMap<String, NodeInstance>();
		for (List<NodeInstance> domainLayer : domainLayers) {
			for (NodeInstance nodeInstance : domainLayer) {
				if (!nodePool.containsKey(nodeInstance.id))
					nodePool.put(nodeInstance.id, nodeInstance);
				else {
					NodeInstance poolInstance = nodePool.get(nodeInstance.id);
					if (poolInstance.cpt == null
							&& nodeInstance.cpt != null)
						poolInstance.cpt = nodeInstance.cpt;
					poolInstance.name = poolInstance.name
							+ String.format("::%s[d=%d]",
									nodeInstance.templateNodeName,
									nodeInstance.domainSlice);
				}
			}
		}
		return nodePool;
	}
}
