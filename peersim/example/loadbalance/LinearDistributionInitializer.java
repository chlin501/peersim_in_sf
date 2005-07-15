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
import peersim.vector.SingleValue;

public class LinearDistributionInitializer  implements Control {

////////////////////////////////////////////////////////////////////////////
// Constants
////////////////////////////////////////////////////////////////////////////

/** 
 * String name of the parameter used to determine the upper bound of the
 * values.
 */
public static final String PAR_MAX = "max";

/** 
 * String name of the parameter used to determine the lower bound of the
 * values. Defaults to -max.
 */
public static final String PAR_MIN = "min";

/** 
 * String name of the parameter that defines the protocol to initialize.
 * Parameter read will has the full name
 * <tt>prefix+"."+PAR_PROT</tt>
 */
public static final String PAR_PROT = "protocol";

private final double max;

private final double min;

private final int protocolID;

////////////////////////////////////////////////////////////////////////////
// Initialization
////////////////////////////////////////////////////////////////////////////

public LinearDistributionInitializer(String prefix)
{
	max = Configuration.getDouble(prefix+"."+PAR_MAX);
	min = Configuration.getDouble(prefix+"."+PAR_MIN,-max);
	protocolID = Configuration.getPid(prefix+"."+PAR_PROT);
}

////////////////////////////////////////////////////////////////////////////
// Methods
////////////////////////////////////////////////////////////////////////////


// Comment inherited from interface
public boolean execute() {
	double step = (max-min)/(Network.size()-1);
	double sum = 0.0;
	double tmp;
	for(int i=0; i<Network.size(); ++i)
	{
		tmp = i*step+min;
		sum += tmp;
		((SingleValue)Network.get(i).getProtocol(protocolID)
			).setValue(tmp);
	}
	return false;
}

}
