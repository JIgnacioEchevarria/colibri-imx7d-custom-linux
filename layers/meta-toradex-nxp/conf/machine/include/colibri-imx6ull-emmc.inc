IMAGE_CLASSES:append = " image_type_tezi"
IMAGE_FSTYPES:append = " teziimg"
# The payload offsets are given in number of 512 byte blocks.
OFFSET_BOOTROM_PAYLOAD = "2"
TORADEX_PRODUCT_IDS = "0062"
TORADEX_FLASH_TYPE = "emmc"

WIC_CREATE_EXTRA_ARGS:append = " --no-fstab-update"

MACHINE_NAME = "Colibri-iMX6ULL-eMMC"

TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT:use-mainline-bsp = " \
	colibri-imx6ull_vga-640x480_overlay.dtbo \
"
TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT = ""

PREFERRED_PROVIDER_virtual/kernel = "linux-toradex"
PREFERRED_PROVIDER_virtual/kernel:preempt-rt = "linux-toradex"
PREFERRED_PROVIDER_virtual/kernel:use-mainline-bsp = "linux-toradex-upstream"
PREFERRED_VERSION_linux-toradex-upstream = "6.6%"
PREFERRED_PROVIDER_virtual/kernel:use-mainline-bsp:preempt-rt = "linux-toradex-upstream-rt"
PREFERRED_VERSION_linux-toradex-upstream-rt = "6.6%"
PREFERRED_PROVIDER_virtual/dtb:use-mainline-bsp = "device-tree-overlays-mainline"

IMX_DEFAULT_BOOTLOADER:colibri-imx6ull-emmc = "u-boot-toradex"
PREFERRED_PROVIDER_u-boot-default-script = "u-boot-distro-boot"

UBOOT_DTB_LOADADDRESS:colibri-imx6ull-emmc = "0x88200000"
UBOOT_DTBO_LOADADDRESS:colibri-imx6ull-emmc = "0x88300000"
UBOOT_ENTRYPOINT:colibri-imx6ull-emmc = "0x84200000"
UBOOT_MAKE_TARGET:colibri-imx6ull-emmc = "u-boot.imx"
UBOOT_RD_LOADADDRESS:colibri-imx6ull-emmc = "0x8A400000"
UBOOT_SUFFIX:colibri-imx6ull-emmc = "imx"
