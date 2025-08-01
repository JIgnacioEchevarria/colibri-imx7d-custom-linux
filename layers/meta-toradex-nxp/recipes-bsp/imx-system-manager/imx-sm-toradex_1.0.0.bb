SUMMARY = "i.MX System Manager Firmware for Toradex Hardware"
DESCRIPTION = "\
The System Manager (SM) is a firmware that runs on a Cortex-M processor on \
many NXP i.MX processors. The Cortex-M is the boot core, runs the boot ROM \
which loads the SM (and other boot code), and then branches to the SM. The \
SM then configures some aspects of the hardware such as isolation mechanisms \
and then starts other cores in the system. After starting these cores, it \
enters a service mode where it provides access to clocking, power, sensor, \
and pin control via a client RPC API based on ARM's System Control and \
Management Interface (SCMI)."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=b66f32a90f9577a5a3255c21d79bc619"

SRC_URI = "${IMX_SYSTEM_MANAGER_SRC};branch=${SRCBRANCH}"
IMX_SYSTEM_MANAGER_SRC = "git://gitlab.int.toradex.com/rd/linux-bsp/imx-sm-toradex.git;protocol=https"
SRCBRANCH = "main"
SRCREV = "922f8affacd89c3d7912021297a871ae0dd70947"
SRCREV:use-head-next = "${AUTOREV}"

S = "${WORKDIR}/git"

require dynamic-layers/arm-toolchain/recipes-bsp/imx-system-manager/imx-system-manager.inc

# Disable serial debug monitor
PACKAGECONFIG = "m0"
