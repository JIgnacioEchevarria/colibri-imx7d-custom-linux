HOMEPAGE = "mediainfo.sourceforge.net"
SUMMARY = "Mediainfo is a tool to analyze multimedia files"
LICENSE = "Apache-2.0|LGPL-2.1-or-later|GPL-2.0-or-later|MPL-2.0"

inherit autotools-brokensep pkgconfig

DEPENDS = "zlib"
SRC_URI = "https://mediaarea.net/download/binary/mediainfo/${PV}/MediaInfo_CLI_${PV}_GNU_FromSource.tar.bz2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/MediaInfo_CLI_GNU_FromSource/MediaInfo/License.html;md5=d9af910c0a24603839b95a00d63c1ecc"

SRC_URI[sha256sum] = "e96633cfced36e7810fc5cd0f15a83362be1f4670e0b38971d8172003dd068d3"
