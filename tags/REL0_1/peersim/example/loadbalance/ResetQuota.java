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

package example.loadbalance;

import peersim.config.*;
import peersim.core.*;
import peersim.dynamics.Dynamics;

/** This class restores the quota value at each node in the topology in order
*   to make the next cycle feasible. 
*/
public class ResetQuota implements Dynamics {

////////////////////////////////////////////////////////////////////////////
// Constants
////////////////////////////////////////////////////////////////////////////

/** 
 * String name of the parameter used to determine the load at
 * the peak node. Parameter read has the full name
 * <tt>prefix+"."+PAR_VALUE</tt>
 */
public static final String PAR_VALUE = "value";

/** 
 * String name of the parameter that defines the protocol to initialize.
 * Parameter read will has the full name
 * <tt>prefix+"."+PAR_PROT</tt>
 */
public static final String PAR_PROT = "protocol";

private final double value;

private final int protocolID;

////////////////////////////////////////////////////////////////////////////
// Initialization
////////////////////////////////////////////////////////////////////////////

public ResetQuota(String prefix)
{
	value = Configuration.getDouble(prefix+"."+PAR_VALUE);
	protocolID = Configuration.getInt(prefix+"."+PAR_PROT);
}

////////////////////////////////////////////////////////////////////////////
// Methods
////////////////////////////////////////////////////////////////////////////


// Comment inherited from interface
public void modify() {
	for(int i=0; i<Network.size(); ++i)
	{
		((BasicBalance)Network.get(i).getProtocol(protocolID)).resetQuota();
	}
}

}