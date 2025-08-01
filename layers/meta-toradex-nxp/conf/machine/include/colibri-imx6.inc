IMAGE_CLASSES:append = " image_type_tezi"
IMAGE_FSTYPES:append = " teziimg"
# The payload offsets are given in number of 512 byte blocks.
OFFSET_BOOTROM_PAYLOAD = "2"
OFFSET_SPL_PAYLOAD = "138"
TORADEX_PRODUCT_IDS = "0014 0015 0016 0017 0077 0078 0079 0080"

MACHINEOVERRIDES =. "tdx:"

MACHINE_NAME = "Colibri-iMX6"

# with kernel 6.5 the device trees were moved to vendor subdirs
KERNEL_DTB_PREFIX = "nxp/imx/"
KERNEL_DTB_PREFIX:preempt-rt = "nxp/imx/"
KERNEL_DTB_PREFIX:use-nxp-bsp = ""
KERNEL_DEVICETREE:colibri-imx6 = " \
    ${KERNEL_DTB_PREFIX}imx6dl-colibri-aster.dtb \
    ${KERNEL_DTB_PREFIX}imx6dl-colibri-eval-v3.dtb \
    ${KERNEL_DTB_PREFIX}imx6dl-colibri-iris.dtb \
    ${KERNEL_DTB_PREFIX}imx6dl-colibri-iris-v2.dtb \
    ${KERNEL_DTB_PREFIX}imx6dl-colibri-v1.2-aster.dtb \
    ${KERNEL_DTB_PREFIX}imx6dl-colibri-v1.2-eval-v3.dtb \
    ${KERNEL_DTB_PREFIX}imx6dl-colibri-v1.2-iris.dtb \
    ${KERNEL_DTB_PREFIX}imx6dl-colibri-v1.2-iris-v2.dtb \
"
TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT:use-mainline-bsp = " \
    colibri-imx6_vga-640x480_overlay.dtbo \
"
TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT = ""

PREFERRED_PROVIDER_virtual/kernel = "linux-toradex"
PREFERRED_PROVIDER_virtual/kernel:preempt-rt = "linux-toradex"
PREFERRED_PROVIDER_virtual/kernel:use-mainline-bsp = "linux-toradex-upstream"
PREFERRED_VERSION_linux-toradex-upstream = "6.6%"
PREFERRED_PROVIDER_virtual/kernel:use-mainline-bsp:preempt-rt = "linux-toradex-upstream-rt"
PREFERRED_VERSION_linux-toradex-upstream-rt = "6.6%"
PREFERRED_PROVIDER_virtual/dtb:use-mainline-bsp = "device-tree-overlays-mainline"

IMX_DEFAULT_BOOTLOADER:colibri-imx6 = "u-boot-toradex"
PREFERRED_PROVIDER_u-boot-default-script = "u-boot-distro-boot"
UBOOT_SUFFIX = "img"

MACHINE_FIRMWARE:remove = "firmware-imx-epdc"

UBOOT_DTB_LOADADDRESS:colibri-imx6 = "0x18200000"
UBOOT_DTBO_LOADADDRESS:colibri-imx6 = "0x18300000"
UBOOT_ENTRYPOINT:colibri-imx6 = "0x14200000"
UBOOT_RD_LOADADDRESS:colibri-imx6 = "0x1A400000"
