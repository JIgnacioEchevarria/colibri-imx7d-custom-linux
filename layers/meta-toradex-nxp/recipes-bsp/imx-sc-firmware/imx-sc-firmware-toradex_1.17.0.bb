# Copyright (C) 2016 Freescale Semiconductor
# Copyright (C) 2017-2019 NXP
# Copyright (C) 2020 Toradex

DESCRIPTION = "i.MX System Controller Firmware for Toradex hardware"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://COPYING;md5=ca53281cc0caa7e320d4945a896fb837"
FSL_EULA_FILE_MD5SUM_LA_OPT_NXP_SOFTWARE_LICENSE_V57 = "ca53281cc0caa7e320d4945a896fb837"
FSL_EULA_FILE_MD5SUMS:append = " \
    ${FSL_EULA_FILE_MD5SUM_LA_OPT_NXP_SOFTWARE_LICENSE_V57} \
"
SECTION = "BSP"

inherit deploy

SRC_URI = "git://github.com/toradex/i.MX-System-Controller-Firmware.git;branch=master;protocol=https;fsl-eula=true"

SRCREV = "79416bcaa86f08c85c7af7d231fd13f68611b528"
SRCREV:use-head-next = "${AUTOREV}"

S = "${WORKDIR}/git"

PROVIDES = "imx-sc-firmware"
RREPLACES:${PN} = "imx-sc-firmware"
RPROVIDES:${PN} = "imx-sc-firmware"
RCONFLICTS:${PN} = "imx-sc-firmware"

BOARD_TYPE ?= "unknown"
SC_FIRMWARE_NAME:mx8qm-generic-bsp = "mx8qm-${BOARD_TYPE}-scfw-tcm.bin"
SC_FIRMWARE_NAME:mx8qxp-generic-bsp = "mx8qx-${BOARD_TYPE}-scfw-tcm.bin"
symlink_name = "scfw_tcm.bin"

BOOT_TOOLS = "imx-boot-tools"

do_compile[noexec] = "1"

do_install[noexec] = "1"

do_deploy() {
    install -Dm 0644 ${S}/src/scfw_export_*/build_*/${SC_FIRMWARE_NAME} ${DEPLOYDIR}/${BOOT_TOOLS}/${SC_FIRMWARE_NAME}
    ln -sf ${SC_FIRMWARE_NAME} ${DEPLOYDIR}/${BOOT_TOOLS}/${symlink_name}
}
addtask deploy after do_install

INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(apalis-imx8.*|colibri-imx8.*)"

