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

package peersim.config;

import java.util.*;

import peersim.cdsim.*;
import peersim.edsim.*;
import peersim.util.*;


/**
* This is the main entry point to peersim. This class loads configuration and
* detects the simulation type. According to this, it invokes the appropriate
* simulator. The known simulators at this moment, along with the way to
* detect them are the following:
* <ul>
* <li>{@link CDSimulator}:
* if {@link CDSimulator#isConfigurationCycleDriven} returns
* true</li>
* <li>{@link EDSimulator}:
* if {@link EDSimulator#isConfigurationEventDriven} returns
* true
* </li>
* </ul>
* This list represents the order in which these alternatives are checked.
* That is, if more than one return true, then the first will be taken.
* Note that this class checks only for these clues and does not check if the
* configuration is consistent or valid.
* @see #main
*/
public class CheckConfig {

//========================== parameters ================================
//======================================================================

/**
 * This is the prefix of the config properties whose value vary during
 * a set of experiments.
 * @config
 */
private static final String PAR_RANGE = "range";


// ========================== static constants ==========================
// ======================================================================

/** {@link CDSimulator} */
protected static final int CDSIM = 0;

/** {@link EDSimulator} */
protected static final int EDSIM = 1;

protected static final int UNKNOWN = -1;

/** the class names of simulators used */
protected static final String[] simName = {
	CDSimulator.class.getCanonicalName(),
	EDSimulator.class.getCanonicalName(),
};



	
// ========================== methods ===================================
// ======================================================================

/**
* Returns the numeric id of the simulator to invoke. At the moment this can
* be {@link #CDSIM}, {@link #EDSIM} or {@link #UNKNOWN}.
*/
protected static int getSimID() {
	
	if( CDSimulator.isConfigurationCycleDriven())
	{
		return CDSIM;
	}
	else if( EDSimulator.isConfigurationEventDriven() )
	{	
		return EDSIM;
	}
	else	return UNKNOWN;
}

// ----------------------------------------------------------------------

/**
* Loads the configuration and check whether all parameters are
* present.
* <p>
* Loading the configuration is currently done with the help of constructing
* an instance of {@link ParsedProperties} using the constructor
* {@link ParsedProperties#ParsedProperties(String[])}.
* The parameter
* <code>args</code> is simply passed to this class. This class is then used
* to initialize the configuration.
* <p>
* After loading the configuration, the experiments are run by invoking the
* appropriate engine, which is identified as follows:
* <ul>
* <li>{@link CDSimulator}:
* if {@link CDSimulator#isConfigurationCycleDriven} returns
* true</li>
* <li>{@link EDSimulator}:
* if {@link EDSimulator#isConfigurationEventDriven} returns
* true
* </li>
* </ul>
* <p>
* This list represents the order in which these alternatives are checked.
* That is, if more than one return true, then the first will be taken.
* Note that this class checks only for these clues and does not check if the
* configuration is consistent or valid.
* @param args passed on to
* {@link ParsedProperties#ParsedProperties(String[])}
* @see ParsedProperties
* @see Configuration
* @see CDSimulator
* @see EDSimulator
*/
public static void main(String[] args)
  throws Exception
{
	System.setErr(new NullPrintStream());
	Properties prop = new ParsedProperties(args);
	Configuration.setConfig( prop, true );
	parseRanges(prop);
	
	final int SIMID = getSimID();
	if( SIMID == UNKNOWN )
	{
		System.err.println(
		    "Simulator: unable to identify configuration, exiting.");
		return;
	}
	
	try {
	
		// XXX could be done through reflection, but
		// this is easier to read.
		switch(SIMID)
		{
		case CDSIM:
			CDSimulator.nextExperiment(true);
			break;
		case EDSIM:
			EDSimulator.nextExperiment(true);
			break;
		}
	
	} catch (MissingParameterException e) {
		System.out.println(e.getMessage());
		System.exit(1);
	} catch (IllegalParameterException e) {
		System.out.println(e.getMessage());
		System.exit(1);
	}	
}

/**
 * Parses a collection of range specifications, identifies the set
 * of parameters that will change during the simulation and
 * instantiates them with the first value of their ranges.
 */
private static void parseRanges(Properties prop)
{
	// Get ranges
	String[] ranges = Configuration.getNames(PAR_RANGE);

	for (int i = 0; i < ranges.length; i++) {
		String[] array = Configuration.getString(ranges[i]).split(";");
		if (array.length != 2) {
			throw new IllegalParameterException(ranges[i],
					" should be formatted as <parameter>;<value list>");
		}
		String[] values = StringListParser.parseList(array[1]);
		prop.setProperty(array[0], values[0]);
	}
}

}
