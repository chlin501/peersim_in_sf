package peersim.graph;

import java.util.Collection;

/**
* A general graph interface. It follows the following model:
* the graph has n nodes which are indexed from 0 to n-1.
* The parameters of operators refer to indeces only.
* Implementations might return
* objects that represent the nodes or edges, though it is not required.
*
* Unidrected graphs are modelled by the interface as directed graphs in which
* all edge (i,j) has a corresponding reverse edge (j,i).
*/
public interface Graph {

	/**
	* Returns true if there is a directed edge between node i
	* and node j.
	*/
	boolean isEdge(int i, int j);

	/**
	* Returns an unmodifyable collection view to all outgoing edges from
	* i. The objects are of type Integer.
	*/
	Collection getNeighbours(int i);

	/**
	* Returns the node object associated with the index. Optional
	* operation.
	*/
	Object getNode(int i);
	
	/**
	* Returns the edge object associated with the index. Optional
	* operation.
	*/
	Object getEdge(int i, int j);

	/**
	* The number of nodes in the graph.
	*/
	int size();

	/**
	* Returns true if the graph is directed othewise false.
	*/
	boolean directed();

	/**
	* Sets given edge, returns true if it did not exist before.
	* If the graph is
	* undirected, sets the edge (j,i) as well. Optional operation.
	*/
	public boolean setEdge(int i, int j);

	/**
	* Removes given edge, returns true if it existed before. If the graph is
	* undirected, removes the edge (j,i) as well. Optional operation.
	*/
	public boolean clearEdge(int i, int j);

	/**
	* Returns the degree of the given node. If the graph is directed,
	* returns out degree.
	*/
	public int degree(int i);
}
