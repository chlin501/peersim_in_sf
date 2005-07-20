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

package peersim.core;

import java.io.*;
import peersim.config.*;

/**
 * 
 *
 * @author Alberto Montresor
 * @version $Revision$
 */
public class Log
{
	
private static final String PAR_TIME = "log.time";

private static boolean logtime = Configuration.contains(PAR_TIME);

private static String prefix = "";

private static PrintStream stream = System.out;
  
public static void setStream(PrintStream newStream)
{
  	stream = newStream;
}

public static void setPrefix(String newPrefix)
{
	prefix = newPrefix+" ";
}

public static void println(String observerId, String string)
{
	if (logtime) 
		stream.println(observerId + " " + prefix +
		" T " + CommonState.getTime() + string);
  	else
  		stream.println(observerId + " " + prefix + string);
}

}
