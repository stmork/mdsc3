/*
**
**	$Revision$
**	$Date$
**	$Author$
**	$Id$
**
**	Copyright (C) 2010 Steffen A. Mork
**
**	This program and the accompanying materials are made available under the
**	terms of the Eclipse Public License v1.0.
**
**	The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
**
**
*/

package de.morknet.mdsc3.postprocessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.xpand2.output.FileHandle;

public abstract class BaseBeautifier {
	private final Logger         m_Log    = Logger.getLogger(BaseBeautifier.class);
	private final FileHandle     m_FileHandle;
	protected StringBuffer       m_Buffer = new StringBuffer();
	protected final List<String> m_Input  = new ArrayList<String>();
	protected final List<String> m_Output = new ArrayList<String>();
	
	protected BaseBeautifier(FileHandle info)
	{
		m_FileHandle = info;
	
		try
		{
			StringReader   sr = new StringReader(m_FileHandle.getBuffer().toString());
			BufferedReader br = new BufferedReader(sr);
			String         line;
		
			while ((line = br.readLine()) != null)
			{
				m_Input.add(line);
			}
			br.close();
		}
		catch (IOException e)
		{
			m_Log.error(e);
		}
	}

	abstract protected void process();

	public void parse()
	{
		try
		{
			process();
			for (String line : m_Output)
			{
				m_Buffer.append(line).append("\n");
			}
			m_FileHandle.setBuffer(m_Buffer);
		}
		catch(Exception e)
		{
			m_Log.error(e);
		}
	}

	protected String getFilename()
	{
		return m_FileHandle.getAbsolutePath();
	}
}
