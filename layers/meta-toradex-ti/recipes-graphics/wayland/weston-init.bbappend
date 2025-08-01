FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

PACKAGECONFIG:append:k3:tdx-distro = " \
    no-idle-timeout \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11 wayland', 'xwayland','', d)} \
"
PACKAGECONFIG[xwayland] = ",,"

SRC_URI:append:k3 = " \
    file://runWeston \
    file://wayland_env.sh \
    file://weston.ini \
"

do_install:append:k3 () {
    install -d ${D}${bindir}
    install -m 755 ${WORKDIR}/runWeston ${D}${bindir}

    install -d ${D}${sysconfdir}/profile.d
    install -m 0644 ${WORKDIR}/weston.ini ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/wayland_env.sh ${D}${sysconfdir}/profile.d/

    if [ "${@bb.utils.contains('PACKAGECONFIG', 'xwayland', 'yes', 'no', d)}" = "yes" ]; then
        sed -i -e "/^\[core\]/a xwayland=true" ${D}${sysconfdir}/xdg/weston/weston.ini
    fi
}

FILES:${PN}:append:k3 = "${sysconfdir}/profile.d/* ${sysconfdir}/weston.ini"
