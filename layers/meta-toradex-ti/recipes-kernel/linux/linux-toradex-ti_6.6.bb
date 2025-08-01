SUMMARY = "Linux kernel for Toradex TI based modules"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI = " \
    ${LINUX_REPO};protocol=https;branch=${SRCBRANCH};name=machine \
    ${KCONFIG_REPO};protocol=https;type=kmeta;name=meta-toradex-bsp;branch=main;destsuffix=${KMETA} \
"

LINUX_REPO = "git://git.toradex.com/linux-toradex.git"
KCONFIG_REPO = "git://git.toradex.com/linux-toradex-kconfig.git"
KMETA = "kernel-meta-toradex-bsp"
SRCREV_meta-toradex-bsp = "f4e5ca7902bbc59dd7b33f9c29d6a07b2f4b60db"
SRCREV_meta-toradex-bsp:use-head-next = "${AUTOREV}"

S = "${WORKDIR}/git"

# Load USB functions configurable through configfs (CONFIG_USB_CONFIGFS)
KERNEL_MODULE_AUTOLOAD += "${@bb.utils.contains('COMBINED_FEATURES', 'usbgadget', ' libcomposite', '',d)}"

inherit kernel-yocto kernel toradex-kernel-deploy-config toradex-kernel-localversion ti-secdev
LINUX_VERSION = "6.6.94"
# skip, as with use-head-next LINUX_VERSION might be set wrongly
KERNEL_VERSION_SANITY_SKIP = "1"

SCMVERSION ?= "y"
SRCBRANCH = "toradex_ti-linux-6.6.y"
SRCREV_machine = "7642ad3241137a64e88063bd996b080428d2f764"
SRCREV_machine:use-head-next = "${AUTOREV}"

PV = "${LINUX_VERSION}+git${SRCPV}"

DEPENDS += "bc-native"
COMPATIBLE_MACHINE = "k3"

export DTC_FLAGS = "-@"

# Special configuration for remoteproc/rpmsg IPC modules
module_conf_rpmsg_client_sample = "blacklist rpmsg_client_sample"
module_conf_ti_k3_r5_remoteproc = "softdep ti_k3_r5_remoteproc pre: virtio_rpmsg_bus"
module_conf_ti_k3_dsp_remoteproc = "softdep ti_k3_dsp_remoteproc pre: virtio_rpmsg_bus"
KERNEL_MODULE_PROBECONF += "rpmsg_client_sample ti_k3_r5_remoteproc ti_k3_dsp_remoteproc"

# Tell to kernel class that we would like to use our defconfig to configure the kernel.
# Otherwise, the --allnoconfig would be used per default which leads to mis-configured
# kernel.
#
# This behavior happens when a defconfig is provided, the kernel-yocto configuration
# uses the filename as a trigger to use a 'allnoconfig' baseline before merging
# the defconfig into the build.
#
# If the defconfig file was created with make_savedefconfig, not all options are
# specified, and should be restored with their defaults, not set to 'n'.
# To properly expand a defconfig like this, we need to specify: KCONFIG_MODE="--alldefconfig"
# in the kernel recipe include.
KCONFIG_MODE = "--alldefconfig"

# We need to pass it as param since kernel might support more then one
# machine, with different entry points
KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"

###############################################################################
# Apply the RT patch and change the configuration to use PREMPT_RT when the
# preempt-rt override is set.
###############################################################################

# patches get moved into the 'older' directory when superseeded, so provide
# both possible storage locations.
SRC_URI:append:preempt-rt = " \
    ${KERNELORG_MIRROR}/linux/kernel/projects/rt/6.6/older/patch-6.6.94-rt56.patch.xz;name=rt-patch \
"

SRC_URI[rt-patch.sha256sum] = "2279d3f97ac1708a54567fd4de4c75bd68840e9cbd916b9060c9564a390b9fb5"

LINUX_KERNEL_TYPE:preempt-rt = "preempt-rt"
LINUX_VERSION:preempt-rt = "6.6.94-rt56"

# The downloaded RT patch doesn't have a upstream status tag
ERROR_QA:remove:preempt-rt = "patch-status"
