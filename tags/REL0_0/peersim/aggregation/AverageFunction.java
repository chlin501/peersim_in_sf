/*
 * Copyright (c) 2003 The BISON Project
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

package aggregation;

import peersim.util.*;
import peersim.core.*;

/**
 * This class implements the average aggregation functions through an
 * epidemic protocol based on averaging the values stored at two neighbor
 * nodes.
 *
 * @author Alberto Montresor
 * @version $Revision$
 */
public class AverageFunction 
extends AbstractFunction 
{

//--------------------------------------------------------------------------
// Initialization
//--------------------------------------------------------------------------

/**
 * Invokes the parent class' constructor to set up the relation between 
 * this protocol and the Linkable protocol used for communication.
 * 
 * @param prefix string prefix for config properties
 * @param obj configuration object, containing the protocol identifier 
 *  for this protocol.
 */
public AverageFunction(String prefix, Object obj) 
{ 
	super(prefix, obj); 
}

//--------------------------------------------------------------------------

/**
 * Clone the object.
 */
public Object clone() throws CloneNotSupportedException 
{
	return super.clone();
}

//--------------------------------------------------------------------------
// Methods
//--------------------------------------------------------------------------

/**
 * Using a {@link Linkable} protocol choses a neighbor and performs a
 * variance reduction step.
 */
public void nextCycle( Node node, int pid )
{
	int linkableID = Protocols.getLink(pid);
	Linkable linkable = (Linkable) node.getProtocol( linkableID );
	if (linkable.degree() > 0)
	{
		Node peer = linkable.getNeighbor(
				CommonRandom.r.nextInt(linkable.degree()));
		
		// XXX quick and dirty handling of failure
		if (peer.getFailState()!=Fallible.OK) return;
		
		AverageFunction neighbor = (AverageFunction)peer.getProtocol(pid);
		double mean = (this.value + neighbor.value) / 2;
		this.value = mean;
		neighbor.value = mean;
	}
}

//--------------------------------------------------------------------------

}