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

package peersim.util;

import java.util.Random;
import java.lang.Math;

/**
 * Simple distributions not provided by java
 */
public class ExtendedRandom extends Random {

public ExtendedRandom(long seed) { super(seed); }

// -------------------------------------------------------------------------
/**
 * Extracts the next integer, according to a Poisson distribution.
 * 
 * @param mean The expected mean.
 * @return An integer Poisson extraction.
 */
public int nextPoisson(double mean) {
	
	double emean = Math.exp(-1 * mean);
	double product = 1;
	int count = 0;
	int result = 0;
	while (product >= emean) {
		product *= nextDouble();
		result = count;
		count++; // keep result one behind
	}
	return result;
}

// -------------------------------------------------------------------------

/**
* Returns a sample from the exponential distribution with mean b.
* In other words, the it will be approximately true for the returned
* value x that <pre>P(x&lt =X)=1-e^(-X/b)</pre>.
* @param mean The expected mean.
*/
public double nextExponential(double mean) {
	
	return -1 * mean * Math.log(nextDouble());
}

// -------------------------------------------------------------------------

/**
* Implements nextLong(long) the same way nexInt(int) is implemented in Random.
* @param n the bound on the random number to be returned. Must be positive.
* @return a pseudorandom, uniformly distributed long value between 0
* (inclusive) and n (exclusive).
*/
public long nextLong(long n) {

	if (n<=0)
		throw new IllegalArgumentException("n must be positive");
	
	if ((n & -n) == n)  // i.e., n is a power of 2
	{	
		return nextLong()&(n-1);
	}
	
	long bits, val;
	do
	{
		bits = (nextLong()>>>1);
		val = bits % n;
	}
	while(bits - val + (n-1) < 0);
	
	return val;
}

// -------------------------------------------------------------------------

public static void main(String[] args) {

	ExtendedRandom er = new ExtendedRandom(12345678);
	for(int i=0; i<100; ++i)
		System.out.println(er.nextLong(Long.parseLong(args[0])));
	
}
}
