pipeline
{
	agent any

	tools
	{
        jdk 'OpenJDK 11.0'
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

		stage('Deploy')
		{
			steps
			{
				sshagent(['becd95b9-30ad-4ba9-99db-60431bf32568'])
				{
					sh 'bin/sync-snapshot.sh'
				}
			}
		}
	}

	post
	{
		always
		{
			chuckNorris()
			step([$class: 'Mailer', recipients: 'linux-dev@morknet.de'])
		}
	}
}
