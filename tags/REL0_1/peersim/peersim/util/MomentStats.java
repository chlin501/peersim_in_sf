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

/*
 * ExtendedStats.java
 *
 * Created on 9 febbraio 2004, 16.10
 */

package peersim.util;

/**
 * This class provides extended statistical informations about the inspected 
 * distribution. In particular, it provides functions to compute the skewness
 * (the 3rd degree moment) and the kurtosis (4th degree moment).
 *
 * @author  Gian Paolo Jesi
 */
public class MomentStats extends IncrementalStats {
    
    private double cubicsum, quadsum; // incremental sums
    
    /** Creates a new instance of MomentStats */
    public MomentStats() {
        super();
        cubicsum = 0.0;
        quadsum = 0.0;
    }
    
    /** Reset all the statistics inside the object. */
    public void reset() {
        super.reset();
        cubicsum = quadsum = 0.0;
    }
    
    /** Add a value to the statistics. 
     * 
     *@param item value to be added to the statistics.
     */
    public void add(double item) {
        super.add(item);
        cubicsum += item * item * item;
        quadsum += item * cubicsum;
    }
   
    /** Outputs on a single line the superclass statistics plus the 
     * current skewness and kurtosis.
     *
     * @return All the statistics available.
     */
    public String toString() {
        return super.toString()+" "+getSkewness()+" "+getKurtosis();
    }
    
    /** Computes the skewness on the node values distribution and 
     * returns the asymmetry coefficient. It gives an indication about the 
     * distribution symmetry compared to the average.
     *
     *@return The skewness value as a double.
     */ 
    public double getSkewness() {
        int n = this.getN();
        double m3 = (((double)n) / (n-1)) * (cubicsum/n - Math.pow(getAverage(), 3) );
        return ( m3 / Math.pow(getStD(), 3 ) );
    }
    
    /** Computes the kurtosis on the node values distribution and 
     *  returns the flatness coefficient. It gives an indication about the 
     *  distribution sharpness or flatness.
     *
     * @return The kurtosis momentus value as a double.
     */ 
    public double getKurtosis(){
        int n = this.getN();
        double m4 = (((double)n) / (n-1)) * (quadsum/n - Math.pow(getAverage(), 4) );
        return ( m4 / Math.pow(getStD(), 4) )-3;
    }

}
