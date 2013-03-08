package edu.pitt.sis.cls.bnt;

import java.util.List;

import edu.pitt.sis.cls.bnt.lang.CptSegment;

public class NodeInstance {
	/**
	 * Unique, alphanumeric identifier.
	 */
	public String id;

	/**
	 * Full display name (i.e., "anne::child[d=0]::mother[d=1]")
	 */
	public String name;

	/**
	 * Conditional probability table.
	 */
	public List<CptSegment> cpt;

	/**
	 * A priori state probabilities.
	 */
	public String apriori;

	/**
	 * Domain slice from which this node was instantiated.
	 */
	public String domainSlice;

	/**
	 * Template node name from which this node was instantiated.
	 */
	public String templateNodeName;
}
