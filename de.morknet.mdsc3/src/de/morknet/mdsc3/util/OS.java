/*
**
**	$Revision$
**	$Date$
**	$Author$
**	$Id$
**
**	Copyright (C) 2016 Steffen A. Mork
**
**	This program and the accompanying materials are made available under the
**	terms of the Eclipse Public License v1.0.
**
**	The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
**
**
*/

package de.morknet.mdsc3.util;

public class OS {
	private final static int    UBUNTU_LEN = "Ubuntu_".length();

	public static double getUbuntuVersion(String os)
	{
		return Double.valueOf(os.substring(UBUNTU_LEN).replace('_', '.'));
	}
}
