package edu.pitt.sis.cls.bnt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.pitt.sis.cls.bnt.lang.CptSegment;
import edu.pitt.sis.cls.bnt.xdsl.Cpt;
import edu.pitt.sis.cls.bnt.xdsl.Nodes;
import edu.pitt.sis.cls.bnt.xdsl.Smile;
import edu.pitt.sis.cls.bnt.xdsl.State;

public class XDSLWriter {
	public String format(Map<String, NodeInstance> nodePool) {
		StringBuffer sb = new StringBuffer();

		Smile smile = new Smile();
		Nodes nodes = smile.getNodes();
		for (String key : nodePool.keySet())
			nodes.getCpt().add(generateCpt(nodePool.get(key)));

		return sb.toString();
	}

	private Cpt generateCpt(NodeInstance nodeInstance) {
		// TODO: Find out whether missing XML elements are null or empty.
		return (nodeInstance.cpt == null || nodeInstance.cpt.size() == 0)
				? generateAPrioriCpt(nodeInstance)
				: generateInfluencedCpt(nodeInstance);
	}

	private Cpt generateAPrioriCpt(NodeInstance nodeInstance) {
		Cpt cpt = new Cpt();
		cpt.setId(nodeInstance.id);
		String[] states = nodeInstance.states.split(" ");
		for (int i = 0; i < states.length; i += 1) {
			State state = new State();
			state.setId(states[i]);
			cpt.getState().add(state);
		}
		cpt.setProbabilities(nodeInstance.apriori);
		return cpt;
	}

	private Cpt generateInfluencedCpt(NodeInstance nodeInstance) {
		throw new RuntimeException("Unimplemented."); // TODO
	}
}
