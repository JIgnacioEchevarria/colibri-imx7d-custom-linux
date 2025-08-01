FILESEXTRAPATHS:prepend := "${THISDIR}/e2fsprogs:"

SRC_URI:append:tdx-distro = " file://e2fsck.conf"

do_install:append:tdx-distro () {
    install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/e2fsck.conf ${D}${sysconfdir}/e2fsck.conf
}

FILES:e2fsprogs-e2fsck:append:tdx-distro = " ${sysconfdir}/e2fsck.conf"
