package edu.pitt.sis.cls.bnt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
		return nodeInstance.cpt == null
				? generateAPrioriCpt(nodeInstance)
				: generateInfluencedCpt(nodeInstance);
	}

	private Cpt generateAPrioriCpt(NodeInstance nodeInstance) {
		List<String> outcomes = new LinkedList<String>();
		List<String> values = new LinkedList<String>();
		String p = nodeInstance.apriori;
		String[] assignments = p.split(";");
		String[] tokens = null;
		for (int i = 0; i < assignments.length; i += 1) {
			tokens = assignments[i].split("=");
			outcomes.add(tokens[0]);
			values.add(tokens[1]);
		}
		Cpt cpt = new Cpt();
		StringBuffer pAcc = new StringBuffer();
		for (int i = 1; i <= outcomes.size(); i += 1) {
			State state = new State();
			state.setId(outcomes.get(i));
			cpt.getState().add(state);
			pAcc.append(values.get(i));
			if (i < outcomes.size())
				pAcc.append(" ");
		}
		cpt.setProbabilities(pAcc.toString());
		return cpt;
	}

	private Cpt generateInfluencedCpt(NodeInstance nodeInstance) {
		throw new RuntimeException("Unimplemented."); // TODO
	}
}
