package de.morknet.mdsc3.ui.wizard;

public class MDSC3ProjectInfo extends org.eclipse.xtext.ui.wizard.DefaultProjectInfo
{
	private String [] tokens;
	private String domain;
	private String name;

	@Override
	public void setProjectName(final String projectName)
	{
		final StringBuffer buffer = new StringBuffer();

		super.setProjectName(projectName);

		tokens = projectName.split("\\.");

		// Extract name
		name = tokens[tokens.length - 1];

		// Build domain from project name
		for (int i = tokens.length - 2; i >= 0; i--)
		{
			buffer.append(tokens[i]);
			if (i > 0)
			{
				buffer.append(".");
			}
		}

		// If there is any domain it's OK. Otherwise build some other domain. 
		domain = buffer.length() > 0 ? buffer.toString() : tokens[0] + ".de";
	}

	public String getDomain()
	{
		return domain.toLowerCase();
	}

	public String getName()
	{
		return name.toLowerCase();
	}
}
