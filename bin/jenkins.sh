#!/bin/bash

DISTRO=${1:-2019-06}
RELEASE=${2:-R}
DOWNLOAD_SERVER=archive.eclipse.org
DOWNLOAD_URI=

#LEVEL=release
LEVEL=snapshot

BASE=$PWD
TARGET_BASE=${BASE}/target
DOWNLOAD=${TARGET_BASE}/download
BUILD=${BASE}
DIST=${TARGET_BASE}/dist
DIRECTOR_ZIP=eclipse-testing-kepler-SR2-linux-gtk-x86_64.tar.gz
DIRECTOR=${TARGET_BASE}/eclipse/eclipse

set -e
mkdir -p $DOWNLOAD $BUILD $DIST

echo "Preparing director..."
if [ ! -e ${DOWNLOAD}/${DIRECTOR_ZIP} ]
then
	URL="http://${DOWNLOAD_SERVER}${DOWNLOAD_URI}/technology/epp/downloads/release/kepler/SR2/${DIRECTOR_ZIP}"
    echo "Downloading $URL..."
    wget -q $URL -O ${DOWNLOAD}/${DIRECTOR_ZIP}
fi

test -d ${TARGET_BASE}/director || tar xfz ${DOWNLOAD}/${DIRECTOR_ZIP} -C ${TARGET_BASE}

function unpack
{
	rm -rf ${BUILD}/?clipse*

	echo "Unpacking... $1"

	case "${1}" in
	*.zip)
		unzip -q ${1} -d $BUILD
		;;
	*.tar)
		tar xf ${1} -C $BUILD
		;;
	*.tar.gz)
		tar xfz ${1} -C $BUILD
		;;
	*.tar.bz2)
		tar xfj ${1} -C $BUILD
		;;
	esac
}

function pack
{
	echo "Packing into... $1"

	cd $BUILD
	case "${1}" in
	*.zip)
		zip -r9 -q ${1} ?clipse*
		;;
	*.tar)
		tar cf ${1} ?clipse*
		;;
	*.tar.gz)
		tar cfz ${1} ?clipse*
		;;
	*.tar.bz2)
		tar cfj ${1} ?clipse*
		;;
	esac
	cd $BASE
}

function build
{
	ECLIPSE="eclipse-java-${DISTRO}-${RELEASE}-$1"
	TARGET=${DIST}/eclipse-mdsc3-${DISTRO}-${RELEASE}-$1

	if [ ! -e ${TARGET} ]
	then
		if [ ! -e ${DOWNLOAD}/${ECLIPSE} ]
		then
			URL="http://${DOWNLOAD_SERVER}${DOWNLOAD_URI}/technology/epp/downloads/release/${DISTRO}/${RELEASE}/${ECLIPSE}"
			echo "Downloading $URL..."
			wget -q $URL -O "${DOWNLOAD}/${ECLIPSE}"
		fi

		unpack ${DOWNLOAD}/${ECLIPSE}
		echo "Prepare Distro ${DISTRO} ${RELEASE}..."
		if [ -d ${BUILD}/eclipse.app ]
		then
			DEST=${BUILD}/Eclipse.app
		else
			DEST=${BUILD}/eclipse
		fi

		echo "################### Installing MDSC3..."
		${DIRECTOR} -noSplash\
			-application org.eclipse.equinox.p2.director\
			-profileProperties org.eclipse.update.install.features=true\
			-repository http://mdsc3.sourceforge.net/updates/${LEVEL}/,http://download.eclipse.org/releases/${DISTRO}\
			-destination ${DEST}\
			-installIU de.morknet.mdsc3.feature.feature.group

#		pack ${TARGET}
	else
		echo "${TARGET} already exists."
	fi
}

build linux-gtk-x86_64.tar.gz
#build linux-gtk.tar.gz
#build macosx-cocoa-x86_64.tar.gz
#build win32-x86_64.zip
#build win32.zip

#rm -rf ${BUILD} ${DOWNLOAD} ${TARGET_BASE}/director
rm -rf ${TARGET}
