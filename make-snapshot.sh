#!/bin/bash

VERSION_OLD=3.0.0
VERSION_NEW=3.0.1

for FILE in de.morknet.mdsc3*/pom.xml
do
	echo $FILE
	sed -e "0,/${VERSION_OLD}/s//${VERSION_NEW}-SNAPSHOT/" -i ${FILE}
done

for FILE in de.morknet.mdsc3.*/feature.xml de.morknet.mdsc3*/META-INF/MANIFEST.MF
do
	echo $FILE
	sed -e "s/${VERSION_OLD}/${VERSION_NEW}.qualifier/g" -i ${FILE}
done

for FILE in de.morknet.mdsc3.updatesite/*.xml
do
	echo $FILE
	sed -e "s/${VERSION_OLD}.qualifier/${VERSION_NEW}.qualifier/g" -i ${FILE}
done

sed -e "s/%RELEASE%/false/g" -e "s/%SNAPSHOT%/true/g" p2.template >de.morknet.mdsc3.feature/p2.inf
