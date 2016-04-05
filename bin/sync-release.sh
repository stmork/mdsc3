#!/bin/bash

chgrp -R  www-data  de.morknet.mdsc3.updatesite/target/repository/
rsync -av --delete de.morknet.mdsc3.updatesite/target/repository/\
	root@scorpius.morknet.de:/var/www/maven/mdsc3/release/
