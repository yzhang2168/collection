package graph;

import java.util.List;
import java.util.ArrayList;

public class GraphNode<E> {
	E key;
	List<GraphNode<E>> neighbors;
	
	public GraphNode(E key) {
		this.key = key;
		neighbors = new ArrayList<GraphNode<E>>();
	}
}
