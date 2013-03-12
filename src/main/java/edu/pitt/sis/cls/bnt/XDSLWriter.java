package edu.pitt.sis.cls.bnt;

import edu.pitt.sis.cls.bnt.xdsl.Cpt;
import edu.pitt.sis.cls.bnt.xdsl.Nodes;
import edu.pitt.sis.cls.bnt.xdsl.Smile;
import edu.pitt.sis.cls.bnt.xdsl.State;

public class XDSLWriter {
	public String format(NodePool nodePool) {
		StringBuffer sb = new StringBuffer();

		Smile smile = new Smile();
		Nodes nodes = smile.getNodes();
		for (String key : nodePool.keySet())
			nodes.getCpt().add(generateCpt(key, nodePool));

		return sb.toString();
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
		// TODO: Find out whether missing XML elements are null or empty:
		return (nodeInstance.cpt == null || nodeInstance.cpt.size() == 0)
				? generateAPrioriCpt(nodeInstance, cpt)
				: generateInfluencedCpt(nodeInstance, cpt, nodePool);
	}

	private Cpt generateAPrioriCpt(NodeInstance nodeInstance, Cpt cpt) {
		cpt.setProbabilities(nodeInstance.apriori);
		return cpt;
	}

	private Cpt generateInfluencedCpt(
			NodeInstance nodeInstance, Cpt cpt, NodePool nodePool) {
		cpt.setProbabilities(nodeInstance.apriori);
		throw new RuntimeException("Unimplemented."); // TODO
	}
}
