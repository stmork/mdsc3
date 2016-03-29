#!/bin/bash

if [ ! -d $1 ]
then
	echo "Missing first directory $1!"
	exit 1
fi

if [ ! -d $2 ]
then
	echo "Missing second directory $2!"
	exit 2
fi

DIR=$PWD

cd $1
for FILE in `find . -type f|sort`
do
	SRC=$1/$FILE
	DST=$DIR/$2/$FILE
	echo $SRC $DST
	diff $SRC $DST |fgrep -v Copyright|fgrep -v "Modelbased"
done
