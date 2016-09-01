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

package peersim.edsim;

import peersim.core.*;
import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;


/**
* This class is used to wrap a {@link CDProtocol} instance into an event so
* that it can be used in the event based simulation engine.
* This class is responsible for calling
* {@link CDProtocol#nextCycle} and also to schedule the consecutive cycle.
* In the configuration of an event driven simulation {@link CDProtocol}s can be
* configured using {@link CDScheduler}, which places appropriate instances of
* this events in the queue.
* <p>
* {@link EDSimulator} treats instances of this class specially, in that
* they are processed when the addressed node is not dead
* ({@link Fallible#DEAD}) whereas normal events are delivered only if the
* node is {@link Fallible#OK}. This is because we might want to schedule
* the next cycle even if the node is temporarily down. This behavior
* can be controlled by the configuration parameters.
* <p>
* Note that reimplementing method {@link #nextDelay} of this class allows
* for arbitrary scheduling,
* including adaptively changing or irregular cycle lengths, etc.
*@see CDScheduler
*@see CDProtocol
*/
public class NextCycleEvent implements Cloneable {


// ========================= fields =================================
// ==================================================================


/**
* If set then the next cycle is scheduled on nodes that are
* not dead ({@link Fallible#DEAD}) at the current time.
* If not set then the next cycle is scheduled only if the node is up
* ({@link Fallible#OK}).
* When the node is dead ({@link Fallible#DEAD}) it is not possible
* to schedule new cycles.
* @config
*/
private static final String PAR_PERSSCH = "persistschedule";

/**
* If set then the current cycle is executed on nodes that are
* not dead ({@link Fallible#DEAD}) at the current time.
* If not set then the current cycle is executed only if the node is up
* ({@link Fallible#OK}).
* When the node is dead ({@link Fallible#DEAD}) it is not possible
* to execute the cycle.
* @config
*/
private static final String PAR_PERSRUN = "persistexec";

protected final boolean perssch;

protected final boolean persrun;

//XXX note that storing these local parameters costs the same as
// storing a reference to a singleton. Static is not an option
// because many different configurations can co-exist.

// =============================== initialization ======================
// =====================================================================


/**
* Default constructor.
*/
public NextCycleEvent() {
	
	persrun = perssch = false;
}

// --------------------------------------------------------------------

/**
* Reads configuration to initialize the object. Extending classes should
* have a constructor with the same signature, often as simple as
* <code>super(n)</code>.
*/
public NextCycleEvent(String prefix) {

	perssch = Configuration.contains(prefix+"."+PAR_PERSSCH);
	persrun = Configuration.contains(prefix+"."+PAR_PERSRUN);
}

// --------------------------------------------------------------------

/**
* Returns a clone of the object. Overriding this method is necessary and
* typically is as simple as <code>return super.clone()</code>. In general,
* always use <code>super.clone()</code> to obtain the object to be returned
* on which you can perform optional deep cloning operations (arrays, etc).
*/
public Object clone() throws CloneNotSupportedException {
	
	return super.clone();
}


// ========================== methods ==================================
// =====================================================================


/**
* Executes the nextCycle method of the protocol, and schedules the next call
* using the delay returned by {@link #nextDelay}.
* If the next execution time as defined by the delay is outside of the
* valid times as defined by {@link CDScheduler#sch}, then the next event is not scheduled.
* Note that this means that this protocol will no longer be scheduled.
* <p>
* The {@link CDProtocol#nextCycle} method will be run only if the node is up, or
* if parameter {@value #PAR_PERSRUN} is set and the node is not dead.
* The next cycle will be scheduled only if the node is up, or
* if parameter {@value #PAR_PERSSCH} is set and the node is not dead.
*/

public final void execute() {

	final int pid = CommonState.getPid();
	final Node node = CommonState.getNode();
	final boolean nodeUp = node.isUp(); // note that dead is not possible here
	
	if( nodeUp || persrun )
	{
		CDProtocol cdp = (CDProtocol)node.getProtocol(pid);
		cdp.nextCycle(node,pid);
	}

	if( nodeUp || perssch ) 
	{
		long delay = nextDelay(CDScheduler.sch[pid].step);
		if( CommonState.getTime()+delay < CDScheduler.sch[pid].until )
			EDSimulator.add(delay, this, node, pid);
	}

}

// --------------------------------------------------------------------

/**
* Calculates the delay until the next execution of the protocol.
* This default implementation returns a constant delay equal to the step
* parameter (cycle length in this case) of the schedule of this event
* (as set in the config file).
*/
protected long nextDelay(long step) {
	
	return step;
}

}


