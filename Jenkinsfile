pipeline
{
	agent any

	tools
	{
        jdk 'jdk8'
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
