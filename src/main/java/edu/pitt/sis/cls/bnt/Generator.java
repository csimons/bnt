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
			Map<String, String> layerSymbols = new HashMap<String, String>();
			for (DomainObject domainObj : slice.getDomainObject())
				for (Node templateNode : bnt.getTemplate().getNode())
					if (templateNode.getName().equals(domainObj.getBinding()))
						layerSymbols.put(templateNode.getName(), domainObj.getName());
			for (DomainObject domainObj : slice.getDomainObject()) {
				for (Node templateNode : bnt.getTemplate().getNode()) {
					if (templateNode.getName().equals(domainObj.getBinding())) {
						NodeInstance nodeInstance = new NodeInstance();
						nodeInstance.id = domainObj.getName();
						nodeInstance.cpt = templateNode.getCptSegment();
						nodeInstance.states = templateNode.getStates();
						nodeInstance.apriori = templateNode.getApriori();
						nodeInstance.templateNodeName = templateNode.getName();
						nodeInstance.domainSlice = slice.getId();
						if (templateNode.getParents() != null)
							nodeInstance.parents = symbolsToInstances(templateNode.getParents(), layerSymbols);
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
		return domainLayers;
	}

	private String symbolsToInstances(String symbols, Map<String, String> resolver) {
		StringBuffer result = new StringBuffer();
		String[] symbolTokens = symbols.split(" ");
		for (int i = 1; i <= symbolTokens.length; i += 1)
			result.append(resolver.get(symbolTokens[i - 1])).append(
					i == symbolTokens.length ? "" : " ");
		return result.toString();
	}

	private NodePool collapseDomainLayers(
			List<List<NodeInstance>> domainLayers) {
		NodePool nodePool = new NodePool();
		for (List<NodeInstance> domainLayer : domainLayers) {
			for (NodeInstance nodeInstance : domainLayer) {
				if (!nodePool.containsKey(nodeInstance.id))
					nodePool.put(nodeInstance.id, nodeInstance);
				else {
					NodeInstance poolInstance = nodePool.get(nodeInstance.id);
					if (poolInstance.cpt.size() == 0
							&& nodeInstance.cpt.size() > 0) {
						poolInstance.cpt = nodeInstance.cpt;
						poolInstance.parents = nodeInstance.parents;
					}
					poolInstance.name = poolInstance.name
							+ String.format("::%s[d=%s]",
									nodeInstance.templateNodeName,
									nodeInstance.domainSlice);
					
				}
			}
		}
		return nodePool;
	}
}
