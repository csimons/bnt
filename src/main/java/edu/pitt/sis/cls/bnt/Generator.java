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
						nodeInstance.id = domainBinding.getValue();
						nodeInstance.cpt = templateNode.getCpt();
						nodeInstance.templateNodeName = templateNode.getName();
						nodeInstance.domainSlice = i;
						nodeInstance.name = String.format("%s::%s[d=%d]",
								nodeInstance.id,
								nodeInstance.templateNodeName,
								nodeInstance.domainSlice);
						domainLayer.add(nodeInstance);
					}
				}
			}
			domainLayers.add(domainLayer);
		}

		Map<String, NodeInstance> pool = new HashMap<String, NodeInstance>();
		for (List<NodeInstance> domainLayer : domainLayers) {
			for (NodeInstance nodeInstance : domainLayer) {
				if (!pool.containsKey(nodeInstance.id))
					pool.put(nodeInstance.id, nodeInstance);
				else {
					NodeInstance poolInstance = pool.get(nodeInstance.id);
					if (poolInstance.cpt.getGiven() == null
							&& nodeInstance.cpt.getGiven() != null)
						poolInstance.cpt = nodeInstance.cpt;
					poolInstance.name = poolInstance.name
							+ String.format("::%s[d=%d]",
									nodeInstance.templateNodeName,
									nodeInstance.domainSlice);
				}
			}
		}
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
