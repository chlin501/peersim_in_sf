/*
 * Copyright (c) 2016 Mark Jelasity
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
		
package peersim.core;

/**
* This Scheduler interface represents a binary function over the time points.
* That is, for each time point it returns a boolean value.
* Implementations should make sure that the implemented schedule is constant,
* that is, independent of how much time has elapsed. In other words,
* calling the method {@link #active(long)} with the same parameter
* should return the same value no matter when it is called during the
* simulation.
*
* Schedulers are used by by the simulator to schedule the execution of
* components.
* 
* The concept of time depends on the simulation model. Current time
* has to be set by the simulation engine, irrespective of the model,
* and can be read using {@link CommonState#getTime()}. This scheduler
* is interpreted over those time points.
*/
public interface SchedulerI {


/** True if and only if the given time point is covered by this scheduler. */
public boolean active(long time);

// -------------------------------------------------------------------

/** True if and only if the current simulation time
* ({@link CommonState#getTime()}) is covered by this scheduler. */
public boolean active();

//-------------------------------------------------------------------

/**
* Implements an iterator over all the active time points.
* The first call will return the first active time point, and subsequent
* calls will return the active times in increasing order.
* If the returned value is negative, there are no more time points.
*/
public long getNext();

//-------------------------------------------------------------------

/** If true then the schedule includes the special time point after
* the simulation. This special time point is not part of the
* simulation schedule, there is no numeric value representing it,
* and it is not returned by {@link getNext}, but it allows
* control components to be run after the simulation has ended.*/
public boolean afterSimulation();

}


