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

package de.morknet.mdsc3.postprocessor.unix;

import org.eclipse.xpand2.output.FileHandle;

import de.morknet.mdsc3.postprocessor.BaseBeautifier;

public class ShellBeautifier extends BaseBeautifier
{
	ShellBeautifier(FileHandle info)
	{
		super(info);
	}

	@Override
	protected void process()
	{
		for (String line : m_Input)
		{
			if (line.trim().length() > 0)
			{
				if (line.trim().equals("#"))
				{
					m_Output.add("");
				}
				else
				{
					m_Output.add(line);
				}
			}
		}
	}

	static boolean isShellScript(FileHandle info)
	{
		String path = info.getAbsolutePath();
		String name = info.getAbsolutePath();
		
		return
			path.contains("init.d") ||
			name.endsWith(".sh") ||
			name.endsWith(".csh") ||
			name.equals("pre-commit") ||
			name.equals("ntp-restart");
	}
}
