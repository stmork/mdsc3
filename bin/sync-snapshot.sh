#!/bin/bash

DST=/var/www/maven/mdsc3

chgrp -R  www-data  de.morknet.mdsc3.updatesite/target/repository/
rsync -av --delete de.morknet.mdsc3.updatesite/target/repository/\
	root@scorpius.morknet.de:${DST}/snapshot/
ssh root@scorpius.morknet.de chown -R jboss.www-data ${DST}/
ssh root@scorpius.morknet.de chmod -R g+rwX ${DST}/

rsync -av --delete de.morknet.mdsc3.updatesite/target/repository/\
	stmork,mdsc3@web.sourceforge.net:/home/project-web/mdsc3/htdocs/updates/snapshot/
