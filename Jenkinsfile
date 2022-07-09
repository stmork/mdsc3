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
