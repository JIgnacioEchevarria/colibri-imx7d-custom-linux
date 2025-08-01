FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://toradex-save-touchscreen-calibration.sh"

PACKAGECONFIG[touchscreen-calibration] = ",,"
PACKAGECONFIG:append = " touchscreen-calibration"

do_install:append () {
    if [ "${@bb.utils.contains('PACKAGECONFIG', 'touchscreen-calibration', 'yes', 'no', d)}" = "yes" ]; then
        sed -i -e "s/^#\\[libinput\\]/[libinput]/g" ${D}${sysconfdir}/xdg/weston/weston.ini

        sed -i -e "/calibration_helper=/d" ${D}${sysconfdir}/xdg/weston/weston.ini
        sed -i -e "/^\[libinput\]/a calibration_helper=${bindir}/toradex-save-touchscreen-calibration" ${D}${sysconfdir}/xdg/weston/weston.ini

        sed -i -e "/touchscreen_calibrator=/d" ${D}${sysconfdir}/xdg/weston/weston.ini
        sed -i -e "/^\[libinput\]/a touchscreen_calibrator=true" ${D}${sysconfdir}/xdg/weston/weston.ini

        sed -i -e "/enable_tap=/d" ${D}${sysconfdir}/xdg/weston/weston.ini
        sed -i -e "/^\[libinput\]/a enable_tap=true" ${D}${sysconfdir}/xdg/weston/weston.ini

        install -Dm0755 ${WORKDIR}/toradex-save-touchscreen-calibration.sh ${D}${bindir}/toradex-save-touchscreen-calibration
    fi
}
