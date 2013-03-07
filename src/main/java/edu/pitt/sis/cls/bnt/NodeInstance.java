package edu.pitt.sis.cls.bnt;

import edu.pitt.sis.cls.bnt.lang.Cpt;

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
	 * Conditional probability table; a-prior tables will lack "given" property.
	 */
	public Cpt cpt;

	/**
	 * Domain slice from which this node was instantiated.
	 */
	public int domainSlice;

	/**
	 * Template node name from which this node was instantiated.
	 */
	public String templateNodeName;
}
