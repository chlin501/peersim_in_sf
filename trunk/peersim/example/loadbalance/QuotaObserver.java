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

package example.loadbalance;

import peersim.config.*;
import peersim.core.*;
import peersim.util.*;

/**
 * This class prints statistics about the actual quota consumptions. It is
 * assumed that the network nodes are instancies of the
 * {@link example.loadbalance.BasicBalance} class.
 * 
 */
public class QuotaObserver implements Control {

    // //////////////////////////////////////////////////////////////////////////
    // Constants
    // //////////////////////////////////////////////////////////////////////////

    /**
     * The protocol to operate on.
     * 
     * @config
     */
    private final String PAR_PROT = "protocol";

    // //////////////////////////////////////////////////////////////////////////
    // Fields
    // //////////////////////////////////////////////////////////////////////////

    /**
     * The name of this observer in the configuration file. Initialized by the
     * constructor parameter.
     */
    private final String name;

    /** Protocol identifier, obtained from config property {@link #PAR_PROT}. */
    private final int pid;

    /**
     * This object keeps track of the values injected and produces statistics.
     * More details in:
     * 
     * @see peersim.util.IncrementalStats .
     */
    private IncrementalStats stats;

    // //////////////////////////////////////////////////////////////////////////
    // Constructor
    // //////////////////////////////////////////////////////////////////////////

    
    /**
     * Standard constructor that reads the configuration parameters. Invoked by
     * the simulation engine.
     * 
     * @param prefix
     *            the configuration prefix for this class.
     */
    public QuotaObserver(String name) {
        this.name = name;
        pid = Configuration.getPid(name + "." + PAR_PROT);
        stats = new IncrementalStats();
    }

    // //////////////////////////////////////////////////////////////////////////
    // Methods
    // //////////////////////////////////////////////////////////////////////////

    // Comment inherited from interface
    public boolean execute() {
        /* Compute max, min, average */
        for (int i = 0; i < Network.size(); i++) {
            BasicBalance protocol = (BasicBalance) Network.get(i).getProtocol(
                    pid);
            stats.add(protocol.quota);
        }

        /* Printing statistics */
        System.out.println(name + ": " + stats);
        return false;
    }

}
