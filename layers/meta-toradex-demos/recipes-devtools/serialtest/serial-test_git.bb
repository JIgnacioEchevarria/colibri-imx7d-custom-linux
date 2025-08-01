SUMMARY = "Serial test application"
HOMEPAGE = "https://github.com/cbrake/linux-serial-test"
SECTION = "utils"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSES/MIT;md5=544799d0b492f119fa04641d1b8868ed"
SRCREV = "d5d32b0765f0ae78e6ec4cbe6ad0800e470aaf67"

SRC_URI = "git://github.com/cbrake/linux-serial-test.git;branch=master;protocol=https"

S = "${WORKDIR}/git"

inherit cmake
