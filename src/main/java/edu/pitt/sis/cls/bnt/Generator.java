package edu.pitt.sis.cls.bnt;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.pitt.sis.cls.bnt.lang.Bnt;
import edu.pitt.sis.cls.bnt.lang.DomainObject;
import edu.pitt.sis.cls.bnt.lang.Node;
import edu.pitt.sis.cls.bnt.lang.Slice;

public class Generator {
	private final Logger LOG;
	private Map<String, Class<? extends Parser>> parsers;

	public Generator() {
		LOG = Logger.getLogger(this.getClass().getCanonicalName());
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

	public NodePool constructPoolFromFile(
			String sourceFilename) throws Exception {
		InputStream is = new FileInputStream(sourceFilename);
		Parser parser = parsers.get(extension(sourceFilename)).newInstance();
		Bnt bnt = parser.parse(is);
		is.close();
		return constructPool(bnt);
	}

	public NodePool constructPool(Bnt bnt) {
		return collapseDomainLayers(generateDomainLayers(bnt));
	}

	private List<List<NodeInstance>> generateDomainLayers(Bnt bnt) {
		List<List<NodeInstance>> domainLayers = new LinkedList<List<NodeInstance>>();
		for (Slice slice : bnt.getDomain().getSlice()) {
			List<NodeInstance> domainLayer = new LinkedList<NodeInstance>();
			for (DomainObject domainObj : slice.getDomainObject()) {
				for (Node templateNode : bnt.getTemplate().getNode()) {
					LOG.debug("generateDomainLayers: Processing nodeTemplate [" + templateNode.getName() + "]");
					if (!templateNode.getBinding().equals(domainObj.getBinding()))
						continue;
					else {
						NodeInstance nodeInstance = new NodeInstance();
						nodeInstance.id = domainObj.getName();
						nodeInstance.cpt = templateNode.getCptSegment();
						nodeInstance.states = templateNode.getStates();
						nodeInstance.apriori = templateNode.getApriori();
						nodeInstance.parents = templateNode.getParents();
						nodeInstance.templateNodeName = templateNode.getName();
						nodeInstance.domainSlice = slice.getId();
						nodeInstance.name = String.format("%s::%s[d=%s]",
								nodeInstance.id,
								nodeInstance.templateNodeName,
								nodeInstance.domainSlice);
						domainLayer.add(nodeInstance);
					}
				}
			}
			domainLayers.add(domainLayer);
		}
		LOG.debug("domainLayers.size(): " + domainLayers.size());
		return domainLayers;
	}

	private NodePool collapseDomainLayers(
			List<List<NodeInstance>> domainLayers) {
		LOG.debug("collapseDomainLayers(): domainLayers.size(): " + domainLayers.size());
		NodePool nodePool = new NodePool();
		for (List<NodeInstance> domainLayer : domainLayers) {
			LOG.debug("Processing domainLayer with size: " + domainLayer.size());
			for (NodeInstance nodeInstance : domainLayer) {
				LOG.debug(" Processing nodeInstance [" + nodeInstance.id + "]");
				if (!nodePool.containsKey(nodeInstance.id))
					nodePool.put(nodeInstance.id, nodeInstance);
				else {
					NodeInstance poolInstance = nodePool.get(nodeInstance.id);
					if (poolInstance.cpt == null
							&& nodeInstance.cpt != null)
						poolInstance.cpt = nodeInstance.cpt;
					poolInstance.name = poolInstance.name
							+ String.format("::%s[d=%s]",
									nodeInstance.templateNodeName,
									nodeInstance.domainSlice);
					
				}
			}
		}
		LOG.debug("collapseDomainLayers(): nodePool.size(): " + nodePool.size());
		return nodePool;
	}
}
