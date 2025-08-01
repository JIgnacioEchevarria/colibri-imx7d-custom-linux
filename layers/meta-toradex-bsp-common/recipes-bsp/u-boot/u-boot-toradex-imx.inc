# build imx-boot from within U-Boot
inherit ${@oe.utils.ifelse(d.getVar('UBOOT_PROVIDES_BOOT_CONTAINER') == '1', 'imx-boot-container', '')}
# if final boot binary blob is created by U-Boot, create a tagged version
# for wic images
inherit ${@ '' if d.getVar('UBOOT_PROVIDES_BOOT_CONTAINER') == '0' and ('mx8-generic-bsp' in d.getVar('OVERRIDES').split(':') or 'mx9-generic-bsp' in d.getVar('OVERRIDES').split(':')) else 'uuu_bootloader_tag'}

BOOT_TOOLS = "imx-boot-tools"
do_deploy:append:mx8m-generic-bsp() {
    # Deploy u-boot-nodtb.bin and fsl-imx8m*-XX.dtb for mkimage to generate boot binary
    if [ -n "${UBOOT_CONFIG}" ]
    then
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
                    install -d ${DEPLOYDIR}/${BOOT_TOOLS}
                    install -m 0777 ${B}/${config}/u-boot-nodtb.bin  ${DEPLOYDIR}/${BOOT_TOOLS}/u-boot-nodtb.bin-${MACHINE}-${type}
                    uboot_dtb=$(grep "CONFIG_DEFAULT_DEVICE_TREE=" ${B}/${config}/.config | sed 's/CONFIG_DEFAULT_DEVICE_TREE=\"\(.*\)\"/\1.dtb/')
                    if grep -q "^CONFIG_OF_UPSTREAM=y" ${B}/${config}/.config; then
                        install -m 0777 ${B}/${config}/dts/upstream/src/arm64/${uboot_dtb} ${DEPLOYDIR}/${BOOT_TOOLS}
                    else
                        install -m 0777 ${B}/${config}/arch/arm/dts/${uboot_dtb} ${DEPLOYDIR}/${BOOT_TOOLS}
                    fi
                fi
            done
            unset  j
        done
        unset  i
    fi
}
