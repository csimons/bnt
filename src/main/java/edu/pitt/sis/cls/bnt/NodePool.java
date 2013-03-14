package edu.pitt.sis.cls.bnt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NodePool {
	private Map<String, NodeInstance> nodePool;

	public NodePool() {
		nodePool = new HashMap<String, NodeInstance>();
	}

	public void put(String key, NodeInstance value) {
		nodePool.put(key, value);
	}

	/*
	 * Sort the keys per graph dependencies.
	 * 
	 * We need this capability because GeNIe apparently reads XDSL files
	 * in a single pass, requiring nodes to be defined before they are
	 * referenced.
	 * 
	 * TODO: Surely there is a better, well-known graph algorithm for this.
	 */
	public List<String> dependencySortedKeyList() {
		List<String> keys = new LinkedList<String>();
		for (String i : nodePool.keySet())
			keys.add(i);
		List<String> sortedKeys = new LinkedList<String>();
		for (int i = 0; i < keys.size(); i += 1) {
			NodeInstance current = nodePool.get(keys.get(i));
			if (current.parents == null)
				sortedKeys.add(keys.get(i));
			else {
				String[] deps = current.parents.split(" ");
				int nDeps = deps.length;
				int nDepsFound = 0;
				int maxDepIdx = 0;
				for (int j = 0; j < deps.length; j += 1) {
					boolean found = false;
					for (int k = 0; k < sortedKeys.size(); k += 1)
						if (sortedKeys.get(k).equals(deps[j])) {
							found = true;
							maxDepIdx = k > maxDepIdx ? k : maxDepIdx; 
						}
					nDepsFound += found ? 1 : 0;
				}
				if (nDepsFound == nDeps)
					sortedKeys.add(maxDepIdx + 1, keys.get(i));
				else if (i == sortedKeys.size())
					sortedKeys.add(keys.get(i));
			}
		}
		return sortedKeys;
	}

	public NodeInstance get(String key) {
		return nodePool.get(key);
	}

	public Set<String> keySet() {
		return nodePool.keySet();
	}

	public int size() {
		return nodePool.size();
	}

	public boolean containsKey(String key) {
		return nodePool.containsKey(key);
	}

	public void clear() {
		nodePool.clear();
	}
}
