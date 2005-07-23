/*
 * Copyright (c) 2003-2005 The BISON Project
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

package peersim.dynamics;

import peersim.config.Configuration;
import peersim.core.*;

/**
 * A network dynamics manager which can grow networks.
 */
public class DynamicNetwork implements Control
{

// --------------------------------------------------------------------------
// Parameters
// --------------------------------------------------------------------------

/**
 * Config parameter which gives prefix of node initializers.
 * @config
 */
private static final String PAR_INIT = "init";

/**
 * If defined, nodes are substituted (an existing node is removed, a new one is
 * added.
 * @config
 */
private static final String PAR_SUBST = "substitute";

/**
 * The number of nodes to add when nodes are scheduled for addition.
 * @config
 */
private static final String PAR_ADD = "add";

/**
 * Nodes are added until the size specified by this parameter is reached. The
 * network will never exceed this size as a result of this network manager.
 * Defaults to {@link Network#getCapacity()}.
 * @config
 */
private static final String PAR_MAX = "maxsize";

/**
 * Nodes are removed until the size specified by this parameter is reached. The
 * network will never go below this size as a result of this network manager.
 * Defaults to 0.
 * @config
 */
private static final String PAR_MIN = "minsize";

/**
 * If this parameter is present, nodes are not crashed, but just made temporary
 * down.
 * @config
 */
private static final String PAR_DOWN = "down";

// --------------------------------------------------------------------------
// Fields
// --------------------------------------------------------------------------

protected double add;

protected boolean substitute;

protected int minsize;

protected int maxsize;

protected boolean down;

protected NodeInitializer[] inits;

// --------------------------------------------------------------------------
// Protected methods
// --------------------------------------------------------------------------

/**
 * Adds n nodes to the network. Extending classes can implement any algorithm to
 * do that. The default algorithm adds the given number of nodes after calling
 * all the configured initializers on them.
 * 
 * @param n
 *          the number of nodes to add, must be non-negative.
 */
protected void add(int n)
{
	for (int i = 0; i < n; ++i) {
		Node newnode = null;
		try {
			newnode = (Node) Network.prototype.clone();
		} catch (CloneNotSupportedException e) {
			// this is fatal but should never happen because nodes
			// are cloneable
			throw new Error(e + "");
		}
		for (int j = 0; j < inits.length; ++j) {
			inits[j].initialize(newnode);
		}
		Network.add(newnode);
	}
}

// ------------------------------------------------------------------

/**
 * Removes n nodes from the network. Extending classes can implement any
 * algorithm to do that. Based on the PAR_DOWN parameter, the default algorithm
 * removes either set their status to down, or remove them simply by calling
 * {@link Network#remove()}. This is equivalent to permanent failure without
 * any cleanup.
 * 
 * @param n
 *          the number of nodes to remove
 */
protected void remove(int n)
{
	if (down) {
		// Remove random nodes
		for (int i = 0; i < n; ++i) {
			int r;
			do {
				r = CommonState.r.nextInt(Network.size());
			} while (!Network.get(r).isUp());
			Network.get(r).setFailState(Fallible.DOWN);
		}
	} else {
		for (int i = 0; i < n; ++i) {
			Network.swap(Network.size() - 1, CommonState.r.nextInt(Network.size()));
			Network.remove();
		}
	}
}

// --------------------------------------------------------------------------
// Initialization
// --------------------------------------------------------------------------

/**
 * Standard constructor that reads the configuration parameters.
 * Invoked by the simulation engine.
 * @param prefix the configuration prefix for this class
 */
public DynamicNetwork(String prefix)
{
	add = Configuration.getDouble(prefix + "." + PAR_ADD);
	substitute = Configuration.contains(prefix + "." + PAR_SUBST);
	Object[] tmp = Configuration.getInstanceArray(prefix + "." + PAR_INIT);
	inits = new NodeInitializer[tmp.length];
	for (int i = 0; i < tmp.length; ++i) {
		System.out.println("Inits " + tmp[i]);
		inits[i] = (NodeInitializer) tmp[i];
	}
	maxsize = Configuration.getInt(prefix + "." + PAR_MAX, Network.getCapacity());
	minsize = Configuration.getInt(prefix + "." + PAR_MIN, 0);
	down = Configuration.contains(prefix + "." + PAR_DOWN);
}

// --------------------------------------------------------------------------
// Public methods
// --------------------------------------------------------------------------

/**
 * Calls {@link #add} or {@link #remove} with the parameters defined by the
 * configuration.
 */
public final boolean execute()
{
	int tochange;
	if (add == 0)
		return false;
	if (!substitute) {
		if ((maxsize <= Network.size() && add > 0)
				|| (minsize >= Network.size() && add < 0))
			return false;
	}
	int toadd = 0;
	int toremove = 0;
	if (add > 0) {
		toadd = (int) (add < 1 ? add * Network.size() : add);
		if (!substitute && toadd > maxsize - Network.size())
			toadd = maxsize - Network.size();
		if (substitute)
			toremove = toadd;
	} else if (add < 0) {
		toremove = (int) (add > -1 ? -add * Network.size() : -add);
		if (!substitute && toremove > Network.size() - minsize)
			toremove = Network.size() - minsize;
		if (substitute)
			toadd = toremove;
	}
	remove(toremove);
	add(toadd);
	return false;
}

// --------------------------------------------------------------------------

}
