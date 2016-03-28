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

package de.morknet.mdsc3.postprocessor.unix;

import org.eclipse.xpand2.output.FileHandle;

import de.morknet.mdsc3.postprocessor.BaseBeautifier;


public class EmptyLineRemover extends BaseBeautifier
{
	EmptyLineRemover(FileHandle info)
	{
		super(info);
	}

	@Override
	protected void process()
	{
		for (String line : m_Input)
		{
			if (line.length() > 0)
			{
				m_Output.add(line);
			}
		}
	}

	static boolean isLog(FileHandle info)
	{
		String name = info.getAbsolutePath();
		
		return name.endsWith(".log") || name.endsWith("hosts");
	}
}
