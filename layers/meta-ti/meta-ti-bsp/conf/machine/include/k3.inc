# TI K3 Aarch64 profile for Cortex-A53/A72 cores

require conf/machine/include/ti-soc.inc
SOC_FAMILY:append = ":k3"

DEFAULTTUNE ?= "aarch64"
require conf/machine/include/arm/armv8a/tune-cortexa72-cortexa53.inc

require conf/machine/include/ti-bsp.inc

KERNEL_IMAGETYPE = "Image"
KERNEL_IMAGETYPES = "Image fitImage"
KERNEL_CLASSES += "kernel-fitimage"

UBOOT_ARCH = "arm"
UBOOT_ENTRYPOINT = "0x81000000"
UBOOT_LOADADDRESS = "0x81000000"
UBOOT_RD_LOADADDRESS = "0x84000000"
UBOOT_RD_ENTRYPOINT = "0x84000000"
UBOOT_DTB_LOADADDRESS = "0x83000000"
UBOOT_DTBO_LOADADDRESS = "0x83080000"
UBOOT_DTBO_OFFSET = "0x00010000"

SPL_BINARY = "tispl.bin"
SPL_BINARYNAME = "tispl.bin"
UBOOT_SUFFIX = "img"

FIT_HASH_ALG ?= "sha512"
FIT_SIGN_ALG ?= "rsa4096"

EXTRA_IMAGEDEPENDS += "virtual/bootloader"

MACHINE_ESSENTIAL_EXTRA_RDEPENDS += "kernel-image-image"

TFA_PLATFORM = "k3"

# Use the expected value of the ubifs filesystem's volume name in the kernel
# and u-boot.
UBI_VOLNAME = "rootfs"

# List common SoC features, may need to add touchscreen for specific machines
MACHINE_FEATURES = "apm usbgadget usbhost vfat ext2 alsa pci optee-ftpm"

IMAGE_FSTYPES += "tar.xz wic.xz wic.bmap"

IMAGE_BOOT_FILES ?= "${SPL_BINARYNAME} u-boot.${UBOOT_SUFFIX} tiboot3.bin"
IMAGE_EFI_BOOT_FILES ?= "${IMAGE_BOOT_FILES}"

EFI_PROVIDER ?= "grub-efi"
MACHINE_FEATURES += "efi"

WKS_FILE ?= "${@bb.utils.contains("MACHINE_FEATURES", "efi", "sdimage-2part-efi.wks.in", "sdimage-2part.wks", d)}"

TI_WKS_BOOTLOADER_APPEND ?= ""

do_image_wic[depends] += "virtual/bootloader:do_deploy"

SERIAL_CONSOLES = "115200;ttyS0 115200;ttyS2"

FALCON_INCLUDE = ""
FALCON_INCLUDE:ti-falcon = "conf/machine/include/ti-falcon.inc"

require ${FALCON_INCLUDE}
