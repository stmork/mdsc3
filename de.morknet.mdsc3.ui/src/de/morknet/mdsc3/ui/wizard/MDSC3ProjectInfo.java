package de.morknet.mdsc3.ui.wizard;

public class MDSC3ProjectInfo extends org.eclipse.xtext.ui.wizard.DefaultProjectInfo
{
	private String [] tokens;
	private String domain;
	private String name;

	@Override
	public void setProjectName(final String projectName)
	{
		super.setProjectName(projectName);
		final StringBuffer buffer = new StringBuffer();

		tokens = projectName.split("\\.");

		for (int i = 0; i < tokens.length - 1;i++)
		{
			if (i > 0)
			{
				buffer.append(".");
			}
			buffer.append(tokens[i]);
		}
		domain = buffer.toString();
		name = tokens[tokens.length - 1];
	}

	public String getDomain()
	{
		return domain;
	}

	public String getName()
	{
		return name;
	}
}
