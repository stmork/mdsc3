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

import java.io.File;

import org.eclipse.xpand2.output.FileHandle;

import de.morknet.mdsc3.postprocessor.BaseBeautifier;


public class ConfBeautifier extends BaseBeautifier {

	ConfBeautifier(FileHandle info)
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
				if (line.equals("##"))
				{
					m_Output.add("#");
				}
				else if(line.equals("#"))
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

	static boolean isConfiguration(FileHandle info)
	{
		File file = new File(info.getAbsolutePath());
		String name = file.getName();

		return
			name.endsWith(".conf") ||
			name.endsWith(".config") ||
			name.endsWith(".cnf") ||
			name.endsWith(".cfg") ||
			name.endsWith(".cf") ||
			name.endsWith(".dot") ||
			name.endsWith(".link") ||
			name.equals("iftab") ||
			name.equals("interfaces") ||
			name.equals("sudoers") ||
			name.equals("pre-commit") ||
			name.startsWith("common-");
	}
}
