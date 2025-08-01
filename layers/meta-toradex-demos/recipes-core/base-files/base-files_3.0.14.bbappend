FILESEXTRAPATHS:prepend := "${THISDIR}/base-files:"

SRC_URI += " \
    file://disable_systemd_coloroutput.sh \
    file://x11-display-var.sh \
"

do_install:append () {
    install -m 0755 -d ${D}${sysconfdir}/profile.d
    install -m 0644 ${WORKDIR}/disable_systemd_coloroutput.sh ${D}${sysconfdir}/profile.d/
    install -m 0644 ${WORKDIR}/x11-display-var.sh ${D}${sysconfdir}/profile.d/
    # Make /media a symbolic link to /run/media
    rmdir ${D}/media
    ln -sf run/media ${D}/media
}

BASEFILESISSUEINSTALL = ""
