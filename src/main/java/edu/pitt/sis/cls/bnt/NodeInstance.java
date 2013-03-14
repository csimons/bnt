package edu.pitt.sis.cls.bnt;


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
    public String cpt;

    /**
     * Space-delimited list of states.
     */
    public String states;

    /**
     * Space-delimited list of a-priori state probabilities.
     */
    public String apriori;

    /**
     * Space-delimited list of direct parents.
     */
    public String parents;

    /**
     * Domain slice from which this node was instantiated.
     */
    public String domainSlice;

    /**
     * Template node name from which this node was instantiated.
     */
    public String templateNodeName;
}
