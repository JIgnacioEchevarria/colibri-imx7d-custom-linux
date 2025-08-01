FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://minibrowser.desktop"

do_install:append() {
    install -m 0644 -D ${WORKDIR}/minibrowser.desktop ${D}${datadir}/applications/minibrowser.desktop
}
