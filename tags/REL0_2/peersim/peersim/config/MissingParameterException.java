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

package peersim.config;

/**
 * 
 *
 * @author Alberto Montresor
 * @version $Revision$
 */
public class MissingParameterException
extends RuntimeException
{
	public MissingParameterException(String name)
	{
		super("Parameter \"" + name + "\" not found");
	}

	public MissingParameterException(String name, String motivation)
	{
		super("Parameter \"" + name + "\" not found" + motivation);
	}

	public String getMessage() {

		return super.getMessage()+" at "+
			getStackTrace()[1].getClassName()+"."+
			getStackTrace()[1].getMethodName()+":"+
			getStackTrace()[1].getLineNumber();
	}
}