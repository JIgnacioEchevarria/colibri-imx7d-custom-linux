IMAGE_CLASSES:append = " image_type_tezi"
IMAGE_FSTYPES:append = " teziimg"
TORADEX_PRODUCT_IDS = "0032 0033 0041 0081"
TORADEX_PRODUCT_IDS[0032] = "imx7s-colibri-eval-v3.dtb"
TORADEX_PRODUCT_IDS[0033] = "imx7d-colibri-eval-v3.dtb"
TORADEX_PRODUCT_IDS[0041] = "imx7d-colibri-eval-v3.dtb"
TORADEX_PRODUCT_IDS[0081] = "imx7d-colibri-eval-v3.dtb"
TORADEX_FLASH_TYPE = "rawnand"

MACHINE_NAME = "Colibri-iMX7"

# with kernel 6.5 the device trees were moved to vendor subdirs
KERNEL_DTB_PREFIX = "nxp/imx/"
KERNEL_DTB_PREFIX:preempt-rt = "nxp/imx/"
KERNEL_DTB_PREFIX:use-nxp-bsp = ""
KERNEL_DEVICETREE:colibri-imx7 = " \
    ${KERNEL_DTB_PREFIX}imx7d-colibri-aster.dtb \
    ${KERNEL_DTB_PREFIX}imx7d-colibri-eval-v3.dtb \
    ${KERNEL_DTB_PREFIX}imx7d-colibri-iris.dtb \
    ${KERNEL_DTB_PREFIX}imx7d-colibri-iris-v2.dtb \
    ${KERNEL_DTB_PREFIX}imx7s-colibri-aster.dtb \
    ${KERNEL_DTB_PREFIX}imx7s-colibri-eval-v3.dtb \
    ${KERNEL_DTB_PREFIX}imx7s-colibri-iris.dtb \
    ${KERNEL_DTB_PREFIX}imx7s-colibri-iris-v2.dtb \
"

PREFERRED_PROVIDER_virtual/kernel = "linux-toradex"
PREFERRED_PROVIDER_virtual/kernel:preempt-rt = "linux-toradex"
PREFERRED_PROVIDER_virtual/kernel:use-mainline-bsp = "linux-toradex-upstream"
PREFERRED_VERSION_linux-toradex-upstream = "6.6%"
PREFERRED_PROVIDER_virtual/kernel:use-mainline-bsp:preempt-rt = "linux-toradex-upstream-rt"
PREFERRED_VERSION_linux-toradex-upstream-rt = "6.6%"
PREFERRED_PROVIDER_virtual/dtb:use-mainline-bsp = "device-tree-overlays-mainline"

IMX_DEFAULT_BOOTLOADER:colibri-imx7 = "u-boot-toradex"
PREFERRED_PROVIDER_u-boot-default-script = "u-boot-distro-boot"

MACHINE_FIRMWARE:remove = "firmware-imx-epdc"
MACHINE_FIRMWARE:remove:use-mainline-bsp = "firmware-imx-vpu-imx6q firmware-imx-vpu-imx6d"

UBOOT_DTB_LOADADDRESS:colibri-imx7 = "0x88200000"
UBOOT_DTBO_LOADADDRESS:colibri-imx7 = "0x88300000"
UBOOT_ENTRYPOINT:colibri-imx7 = "0x84200000"
UBOOT_MAKE_TARGET:colibri-imx7 = "u-boot-nand.imx"
UBOOT_RD_LOADADDRESS:colibri-imx7 = "0x88400000"
