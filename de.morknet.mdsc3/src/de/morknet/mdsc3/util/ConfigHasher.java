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

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import de.morknet.mdsc3.crypto.MD5Hasher;

public class ConfigHasher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (String filename : args)
		{
			System.err.println(filename);
			File root = new File(filename);
			
			if (root.isDirectory())
			{
				for (File file : root.listFiles())
				{
					if (file.isDirectory())
					{
						Set<File> fileset = new TreeSet<File>();
						
						collect(fileset, file);
						try
						{
							System.out.println(file.getName() + " " + MD5Hasher.hash(fileset));
						}
						catch (IOException e)
						{
							// Prevent output - no further processing necessary.
						}
					}
				}
			}
		}
	}

	private final static void collect(Set<File> set, File file)
	{
		if (file.isFile())
		{
			set.add(file);
		}
		else
		{
			for (File entry : file.listFiles())
			{
				collect (set, entry);
			}
		}
	}
}
