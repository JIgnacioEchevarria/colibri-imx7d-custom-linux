SUMMARY = "ALSA Use Case Manager configuration for Toradex Hardware"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

RDEPENDS:${PN} = "alsa-ucm-conf"

SRC_URI = "file://ucm2/"

do_install () {
        install -d "${D}${datadir}/alsa"
        cp -r "${WORKDIR}/ucm2" "${D}${datadir}/alsa"
}

FILES:${PN} = "${datadir}"
