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
    public ExtendedRandom(long seed) {
        super(seed);
    }

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

    
    public double nextExponential(double b) {
        return -1 * b * Math.log(nextDouble());
    }

    public double nextPower(double base, double a) {
        return base / Math.pow(nextDouble(), a) - base;
    }
}