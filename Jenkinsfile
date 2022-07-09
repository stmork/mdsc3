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
