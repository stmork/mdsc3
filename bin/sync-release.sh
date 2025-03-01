#!/bin/bash

DST=/var/www/maven/mdsc3

chmod -R o+rX de.morknet.mdsc3.updatesite/target/repository/

chgrp -R  www-data  de.morknet.mdsc3.updatesite/target/repository/
rsync -av --delete de.morknet.mdsc3.updatesite/target/repository/\
	root@scorpius.morknet.de:${DST}/release/
ssh root@scorpius.morknet.de chown -R jboss.www-data ${DST}/
ssh root@scorpius.morknet.de chmod -R g+rwX ${DST}/

rsync -av --delete de.morknet.mdsc3.updatesite/target/repository/\
	stmork,mdsc3@web.sourceforge.net:/home/project-web/mdsc3/htdocs/updates/release/
