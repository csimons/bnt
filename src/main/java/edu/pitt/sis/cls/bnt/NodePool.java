package edu.pitt.sis.cls.bnt;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NodePool {
	private Map<String, NodeInstance> nodePool;

	public NodePool() {
		nodePool = new HashMap<String, NodeInstance>();
	}

	public NodePool(Map<String, NodeInstance> nodePool) {
		this.nodePool = nodePool;
	}

	public void put(String key, NodeInstance value) {
		nodePool.put(key, value);
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
