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

package peersim.transport;


/**
 * This static data structure emulates an underlying router network
 * of fixed size, and stores the latency measurements for all pair
 * of routers.
 *
 * @author Alberto Montresor
 * @version $Revision$
 */
public class E2ENetwork
{

//---------------------------------------------------------------------
//Fields
//---------------------------------------------------------------------

/**
 * True if latency between nodes is considered symmetric. False otherwise.
 */
private static boolean symm;	
	
/**
 * Size of the router network. 
 */
private static int size;

/**
 * Latency distances between nodes.
 */
private static int[][] array;
	
//---------------------------------------------------------------------
//Methods
//---------------------------------------------------------------------

/**
 * Resets the network, by creating a triangular (if symm is true) or
 * a rectangular (if symm is false) array of integers.
 */
public static void reset(int size, boolean symm)
{
	E2ENetwork.symm = symm;
	E2ENetwork.size = size;
	array = new int[size][];
	for (int i=0; i < size; i++) {
		if (symm)
			array[i] = new int[i];
		else
			array[i] = new int[size];
	}
}
	
/**
 * Returns the latency associated to the specified (sender, receiver)
 * pair.
 * 
 * @param sender the index of the sender
 * @param receiver the index of the receiver
 * @return
 */
public static int getLatency(int sender, int receiver) 
{
	if (sender==receiver)
		return 0;
  if (symm) {
  	// Symmetric network
  	if (sender < receiver) {
  		int tmp = sender;
  		sender = receiver;
  		receiver = tmp;
  	}
  } 
  return array[sender][receiver];
}

/**
 * Sets the latency associated to the specified (sender, receiver)
 * pair.
 * 
 * @param sender the index of the sender
 * @param receiver the index of the receiver
 * @param latency the latency to be set
 */
public static void setLatency(int sender, int receiver, int latency) 
{
  if (symm) {
  	// Symmetric network
  	if (sender < receiver) {
  		int tmp = sender;
  		sender = receiver;
  		receiver = tmp;
  	}
  } 
 	array[sender][receiver] = latency;
}

/**
 * Returns the current size of the underlying network.
 */
public static int getSize()
{
	return size;
}

}
