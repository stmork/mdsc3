#!/bin/bash

VERSION=3.0.4

for FILE in de.morknet.mdsc3*/pom.xml
do
	echo $FILE
	sed -e "0,/${VERSION}-SNAPSHOT/s//${VERSION}/" -i ${FILE}
done

for FILE in de.morknet.mdsc3.*/feature.xml de.morknet.mdsc3*/META-INF/MANIFEST.MF
do
	echo $FILE
	sed -e "s/${VERSION}.qualifier/${VERSION}/g" -i ${FILE}
done

sed -e "s/%RELEASE%/true/g" -e "s/%SNAPSHOT%/false/g" p2.template >de.morknet.mdsc3.feature/p2.inf
