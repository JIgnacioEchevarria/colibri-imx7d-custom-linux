SUMMARY = "U-Boot bootloader with support for Toradex i.MX 8 series SoMs"
HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"
DEPENDS = "bc-native dtc-native flex-native bison-native python3-setuptools-native"
PROVIDES:append = " u-boot"

require recipes-bsp/u-boot/u-boot.inc

SRC_URI = "git://git.toradex.com/u-boot-toradex.git;protocol=https;branch=${SRCBRANCH}"
SRCREV = "4db7ef83ebd612ba8b548f19e6245041a9faeb37"
SRCREV:use-head-next = "${AUTOREV}"
SRCBRANCH = "toradex_imx_lf_v2024.04"

CVE_PRODUCT = "denx:u-boot"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

BOOT_TOOLS = "imx-boot-tools"

inherit toradex-u-boot-localversion
# build imx-boot from within U-Boot
inherit ${@oe.utils.ifelse(d.getVar('UBOOT_PROVIDES_BOOT_CONTAINER') == '1', 'imx-boot-container', '')}
# if final boot binary blob is created by U-Boot, create a tagged version
# for wic images
inherit ${@ '' if d.getVar('UBOOT_PROVIDES_BOOT_CONTAINER') == '0' and ('mx8-generic-bsp' in d.getVar('OVERRIDES').split(':') or 'mx9-generic-bsp' in d.getVar('OVERRIDES').split(':')) else 'uuu_bootloader_tag'}

UBOOT_INITIAL_ENV = "u-boot-initial-env"

COMPATIBLE_MACHINE = "(colibri-imx8x|apalis-imx8|mx95-generic-bsp)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
