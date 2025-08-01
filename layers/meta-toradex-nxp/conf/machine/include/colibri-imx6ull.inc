IMAGE_CLASSES:append = " image_type_tezi"
IMAGE_FSTYPES:append = " teziimg"
TORADEX_PRODUCT_IDS = "0036 0040 0044 0045"
TORADEX_PRODUCT_IDS[0036] = "imx6ull-colibri-eval-v3.dtb"
TORADEX_PRODUCT_IDS[0040] = "imx6ull-colibri-wifi-eval-v3.dtb"
TORADEX_PRODUCT_IDS[0044] = "imx6ull-colibri-eval-v3.dtb"
TORADEX_PRODUCT_IDS[0045] = "imx6ull-colibri-wifi-eval-v3.dtb"
TORADEX_FLASH_TYPE = "rawnand"

WIC_CREATE_EXTRA_ARGS:append = " --no-fstab-update"

MACHINEOVERRIDES =. "tdx:"

MACHINE_NAME = "Colibri-iMX6ULL"

MACHINE_EXTRA_RRECOMMENDS:append = " mwifiexconf"

# with kernel 6.5 the device trees were moved to vendor subdirs
KERNEL_DTB_PREFIX = "nxp/imx/"
KERNEL_DTB_PREFIX:preempt-rt = "nxp/imx/"
KERNEL_DTB_PREFIX:use-nxp-bsp = ""
KERNEL_DEVICETREE:colibri-imx6ull = " \
    ${KERNEL_DTB_PREFIX}imx6ull-colibri-aster.dtb \
    ${KERNEL_DTB_PREFIX}imx6ull-colibri-eval-v3.dtb \
    ${KERNEL_DTB_PREFIX}imx6ull-colibri-iris.dtb \
    ${KERNEL_DTB_PREFIX}imx6ull-colibri-iris-v2.dtb \
    ${KERNEL_DTB_PREFIX}imx6ull-colibri-wifi-aster.dtb \
    ${KERNEL_DTB_PREFIX}imx6ull-colibri-wifi-eval-v3.dtb \
    ${KERNEL_DTB_PREFIX}imx6ull-colibri-wifi-iris.dtb \
    ${KERNEL_DTB_PREFIX}imx6ull-colibri-wifi-iris-v2.dtb \
"

PREFERRED_PROVIDER_virtual/kernel = "linux-toradex"
PREFERRED_PROVIDER_virtual/kernel:preempt-rt = "linux-toradex"
PREFERRED_PROVIDER_virtual/kernel:use-mainline-bsp = "linux-toradex-upstream"
PREFERRED_VERSION_linux-toradex-upstream = "6.6%"
PREFERRED_PROVIDER_virtual/kernel:use-mainline-bsp:preempt-rt = "linux-toradex-upstream-rt"
PREFERRED_VERSION_linux-toradex-upstream-rt = "6.6%"
PREFERRED_PROVIDER_virtual/dtb:use-mainline-bsp = "device-tree-overlays-mainline"

IMX_DEFAULT_BOOTLOADER:colibri-imx6ull = "u-boot-toradex"
PREFERRED_PROVIDER_u-boot-default-script = "u-boot-distro-boot"

MACHINE_FIRMWARE:remove:use-mainline-bsp = "firmware-imx-vpu-imx6q firmware-imx-vpu-imx6d"

UBOOT_DTB_LOADADDRESS:colibri-imx6ull = "0x88200000"
UBOOT_DTBO_LOADADDRESS:colibri-imx6ull = "0x88300000"
UBOOT_ENTRYPOINT:colibri-imx6ull = "0x84200000"
UBOOT_MAKE_TARGET:colibri-imx6ull = "u-boot-nand.imx"
UBOOT_RD_LOADADDRESS:colibri-imx6ull = "0x8A400000"
