package peersim.graph;

import java.util.*;

/**
* This class implements a graph which uses a bitmatrix as inner representation
* of edges.
*/
public class BitMatrixGraph implements Graph {


// ====================== private fileds ========================
// ==============================================================

private final List sets;

private final boolean directed;

// ====================== public constructors ===================
// ==============================================================


/**
* Constructs a directed graph with the given number of nodes.
* The graph has no edges initially. The graph is directed.
* @param n size of graph
*/
public BitMatrixGraph( int n ) {

	sets = new ArrayList(n);
	for(int i=0; i<n; ++i) sets.add(new BitSet());
	this.directed = true;
}

// ---------------------------------------------------------------

/**
* Constructs an graph with the given number of nodes.
* The graph has no edges initially.
* @param n size of graph
* @param directed if true graph is directed
*/
public BitMatrixGraph( int n, boolean directed ) {

	sets = new ArrayList(n);
	for(int i=0; i<n; ++i) sets.add(new BitSet());
	this.directed = directed;
}


// ======================= Graph implementations ================
// ==============================================================


public boolean isEdge(int i, int j) {
	
	return ((BitSet)sets.get(i)).get(j);
}

// ---------------------------------------------------------------

public Collection getNeighbours(int i) {
	
	Set result = new HashSet();
	BitSet neighb = (BitSet)sets.get(i);
	final int max = size();
	for(int j=0; j<max; ++j)
	{
		if( neighb.get(j) ) result.add( new Integer(j) );
	}

	return Collections.unmodifiableCollection(result);
}

// ---------------------------------------------------------------

/** Returns null always */
public Object getNode(int i) { return null; }
	
// ---------------------------------------------------------------

/**
* Returns null always. 
*/
public Object getEdge(int i, int j) { return null; }

// ---------------------------------------------------------------

public int size() { return sets.size(); }

// --------------------------------------------------------------------
	
public boolean directed() { return directed; }

// --------------------------------------------------------------------

public boolean setEdge(int i, int j) {

	if( i > size() || j > size() || i<0 || j<0 ) throw new
		IndexOutOfBoundsException();

	BitSet neighb = (BitSet)sets.get(i);
	boolean old = neighb.get(j);
	neighb.set(j);
	
	if( !old && !directed )
	{
		neighb = (BitSet)sets.get(j);
		neighb.set(i);
	}
	
	return !old;
}

// --------------------------------------------------------------------

public boolean clearEdge(int i, int j) {

	if( i > size() || j > size() || i<0 || j<0 ) throw new
		IndexOutOfBoundsException();

	BitSet neighb = (BitSet)sets.get(i);
	boolean old = neighb.get(j);
	neighb.clear(j);
	
	if( old && !directed )
	{
		neighb = (BitSet)sets.get(i);
		neighb.clear(j);
	}
	
	return old;
}

// --------------------------------------------------------------------

public int degree(int i) {

	BitSet neighb = (BitSet)sets.get(i);
	return neighb.cardinality(); // only from jdk 1.4
}

}
