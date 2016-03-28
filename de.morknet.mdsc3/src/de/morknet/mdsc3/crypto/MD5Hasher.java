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

package de.morknet.mdsc3.crypto;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MD5Hasher
{
	public final static String hash(Set<File> set) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (File file : set)
		{
			try {
				FileInputStream fis = new FileInputStream(file);
	
				byte [] content = new byte[(int)file.length()];
				fis.read(content);
				baos.write(content);
				
				//				byte [] content = new byte[fis.]
				fis.close();
			} catch (IOException e) {
				System.err.println("Cannot process file " + file);
				throw e;
			}
		}

		return hash(baos.toByteArray());
	}

	private static String hash(byte [] array)
	{
		StringBuffer result = new StringBuffer(32);

		try
		{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(array);
			try(Formatter f = new Formatter(result))
			{
				for (byte b : md5.digest())
				{
					f.format("%02x", b);
				}
			}
		}
		catch (NoSuchAlgorithmException ex)
		{
			ex.printStackTrace();
		}
		System.err.println(array.length);
		return result.toString();		
	}
}
