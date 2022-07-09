pipeline
{
	agent any

	tools
	{
        jdk 'OpenJDK 8.0'
    }

	stages
	{
		stage('Build')
		{
			steps
			{
				withMaven(mavenSettingsFilePath: 'de.morknet.mdsc3.releng/pom.xml')
				{
					sh "mvn clean verify"
				}
			}
		}
	}

	post
	{
		always
		{
			chuckNorris()
		}
	}
}
