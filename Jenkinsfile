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
				withMaven()
				{
					sh "mvn -f de.morknet.mdsc3.releng/pom.xml clean verify"
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
