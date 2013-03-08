package edu.pitt.sis.cls.bnt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.pitt.sis.cls.bnt.lang.CptSegment;
import edu.pitt.sis.cls.bnt.xdsl.Nodes;
import edu.pitt.sis.cls.bnt.xdsl.Smile;

public class XDSLWriter {
	public String format(Map<String, NodeInstance> nodePool) {
		StringBuffer sb = new StringBuffer();

		Smile smile = new Smile();
		Nodes nodes = smile.getNodes();
		for (String key : nodePool.keySet()) {
			NodeInstance nodeInstance = nodePool.get(key);
			if (nodeInstance.cpt == null) {
				// TODO: Build node using a-priori probabilities.
				List<String> states = new LinkedList<String>();
				List<String> values = new LinkedList<String>();
				String p = nodeInstance.apriori;
				String[] assignments = p.split(";");
				String[] tokens = null;
				for (int i = 0; i < assignments.length; i += 1) {
					tokens = assignments[i].split("=");
					states.add(tokens[0]);
					values.add(tokens[1]);
				}
				/*
				 * TODO: Setters not created by XJC.
				 */
//				Cpt cpt = new Cpt();
//				cpt.setState(new LinkedList<State>());
//				for (String state : states) {
//					State state = new State();
//					state.setId(state);
//					cpt.getState().add(state);
//				}
//				nodes.getCpt().add(cpt);
			} else {
				// TODO: Build node by combining CPT segments.
				for (CptSegment cptSegment : nodeInstance.cpt) {
					// ...
				}
			}
		}

		return sb.toString();
	}
}
