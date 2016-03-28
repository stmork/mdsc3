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
import org.eclipse.xpand2.output.PostProcessor;

import de.morknet.mdsc3.postprocessor.BaseBeautifier;

public class UnixBeautifier implements PostProcessor
{
	public static boolean beautify(FileHandle info)
	{
		BaseBeautifier beautifier = null;
		
		if (EmptyLineRemover.isLog(info))
		{
			beautifier = new EmptyLineRemover(info);
		}
		else if (ShellBeautifier.isShellScript(info))
		{
			beautifier = new ShellBeautifier(info);
		}
		else if (ConfBeautifier.isConfiguration(info))
		{
			beautifier = new ConfBeautifier(info);
		}
		
		if (beautifier != null)
		{
			beautifier.parse();
		}
		
		return beautifier != null;
	}

	@Override
	public void beforeWriteAndClose(FileHandle info)
	{
		beautify(info);
	}

	@Override
	public void afterClose(FileHandle impl)
	{
	}
}
