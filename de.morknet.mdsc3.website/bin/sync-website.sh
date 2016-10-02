#!/bin/bash

SRC=/tmp/target

rsync -av --delete src/ $SRC/
rsync -av --delete stmork,mdsc3@web.sourceforge.net:/home/project-web/mdsc3/htdocs/updates/ $SRC/updates/
rsync -av --delete $SRC/ stmork,mdsc3@web.sourceforge.net:/home/project-web/mdsc3/htdocs/
