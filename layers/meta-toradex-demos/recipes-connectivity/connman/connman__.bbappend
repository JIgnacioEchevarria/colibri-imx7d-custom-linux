FILESEXTRAPATHS:prepend := "${THISDIR}/connman:"

SRC_URI:append = " \
    file://0001-Adjust-main-configuration.patch \
    file://0001-connman-clock-ntp-client-should-not-update-time-time.patch \
"

do_install:append() {
    install -d ${D}${sysconfdir}/connman/
    install -m 0644 ${S}/src/main.conf ${D}${sysconfdir}/connman/
}
