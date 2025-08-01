require conf/distro/poky.conf
require conf/distro/include/arm-defaults.inc

DISTRO = "tdx-base"
DISTRO_NAME = "Toradex Distro Base"

TDX_BUILDNBR ?= "0"
TDX_PURPOSE ?= "Testing"

def get_tdx_prerelease(purpose, datetime):
    if purpose == "Testing":
        return '-devel-%s' % (datetime)
    elif purpose == "Nightly":
        return '-devel-%s' % (datetime[:8])
    elif purpose == "Monthly":
        return '-devel-%s' % (datetime[:6])
    elif purpose == "Release":
        return ''
    else:
        return '-devel-%s' % (datetime)

TDX_BUILD ?= "+build.${TDX_BUILDNBR}"
TDX_MATRIX_BUILD_TIME ?= "${DATETIME}"
TDX_MATRIX_BUILD_TIME[vardepsexclude] = "DATETIME"
TDX_PRERELEASE ?= "${@get_tdx_prerelease(d.getVar('TDX_PURPOSE'), d.getVar('TDX_MATRIX_BUILD_TIME'))}"
TDX_MAJOR ?= "7"
TDX_MINOR ?= "3"
TDX_PATCH ?= "0"
TDX_RELEASE ?= "${TDX_MAJOR}.${TDX_MINOR}.${TDX_PATCH}"

# Toradex Version number without date for U-Boot/Kernel and other Toradex
# specific software artifacts.
TDX_VERSION ?= "${TDX_RELEASE}${@'' if d.getVar('TDX_PURPOSE') == 'Release' else '-devel'}"

# Show Tezi EULA license
TEZI_SHOW_EULA_LICENSE ?= "1"
TEZI_SHOW_EULA_LICENSE:use-mainline-bsp ?= "0"
TEZI_SHOW_EULA_LICENSE:colibri-imx6:use-mainline-bsp ?= "1"
TEZI_SHOW_EULA_LICENSE:apalis-imx6:use-mainline-bsp ?= "1"

# Complete image version with date and build number
DISTRO_VERSION = "${TDX_RELEASE}${TDX_PRERELEASE}${TDX_BUILD}"

SDK_VENDOR = "-tdxsdk"
SDK_NAME_PREFIX = "${DISTRO}"
# do not use elements in SDK_VERSION which are in vardepsexclude
SDK_VERSION = "${TDX_RELEASE}"

MAINTAINER = "Toradex <yocto-patches@toradex.com>"

TARGET_VENDOR = "-tdx"

DISTROOVERRIDES .= ":tdx-distro"

# create etc/build in the rootfs
INHERIT += "image-buildinfo"

# Log information on images and packages
INHERIT += "buildhistory"
BUILDHISTORY_COMMIT = "1"

IMAGE_LINGUAS ?= "en-us"

# Some Toradex recipes like linux-toradex, u-boot-toradex,
# device-tree-overlays are appending SCM hashes to PV, version
# numbers going backwards are not a problem as we don't provide
# binary feeds.
#
# This avoids some annoying errors as follows:
# | QA Issue: Package version went backwards which would break package feeds
ERROR_QA:remove = "version-going-backwards"

DISTRO_FEATURES:append = " pam systemd usrmerge tpm2"
DISTRO_FEATURES:remove = "sysvinit ptest"
VIRTUAL-RUNTIME_init_manager = "systemd"
PACKAGECONFIG:append:pn-systemd = " serial-getty-generator"
PACKAGECONFIG:append:pn-qemu-native = " libusb"

PREFERRED_PROVIDER_u-boot-fw-utils = "libubootenv"
PREFERRED_RPROVIDER_u-boot-fw-utils = "libubootenv"

PREFERRED_PROVIDER_virtual/dtb ?= "device-tree-overlays"

PREFERRED_PROVIDER_opencl-headers:imxgpu      = "imx-gpu-viv"
PREFERRED_PROVIDER_opencl-clhpp:imxgpu        = "imx-gpu-viv"
PREFERRED_PROVIDER_opencl-icd-loader:imxgpu   = "imx-gpu-viv"
PREFERRED_RPROVIDER_opencl-icd-loader:imxgpu  = "libopencl-imx"

# keep the dtb vendor directories in fitimage node names
KERNEL_DTBVENDORED = "1"

# Defines the root home directory as /root, as required by systemd.
ROOT_HOME = "/root"
