SUMMARY = "Sets i.MX boot mode on next reboot to recovery, aka serial download"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://bmode_usb.c;endline=5;md5=4c9010cb42fb7648a460c35e6b5c685c"

SRC_URI = "file://bmode_usb.c"
S = "${WORKDIR}"
DEFINE = "NO_MACHINE"
DEFINE:apalis-imx6 = "MX6"
DEFINE:colibri-imx6 = "MX6"
DEFINE:colibri-imx6ull = "MX6ULL"
DEFINE:colibri-imx6ull-emmc = "MX6ULL"

do_compile() {
    ${CC} -o bmode_usb bmode_usb.c ${CFLAGS} ${LDFLAGS} -D ${DEFINE}
}

do_install() {
    install -d ${D}${bindir}
    install bmode_usb ${D}${bindir}
}
