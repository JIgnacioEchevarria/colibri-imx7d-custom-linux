# Copyright (C) 2017-2024 NXP

require imx-mkimage_git.inc

DESCRIPTION = "Generate Boot Loader for i.MX 8 device"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"
SECTION = "BSP"

inherit use-imx-security-controller-firmware uboot-config

DEPENDS += " \
    u-boot \
    ${IMX_EXTRA_FIRMWARE} \
    imx-atf \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os', '', d)} \
"
# xxd is a dependency of fspi_packer.sh
DEPENDS += "xxd-native"
DEPENDS:append:mx8m-generic-bsp = " u-boot-mkimage-native dtc-native"
DEPENDS:append:mx93-generic-bsp = " u-boot-mkimage-native dtc-native"

inherit deploy uuu_bootloader_tag

UUU_BOOTLOADER = "imx-boot"

# Add CFLAGS with native INCDIR & LIBDIR for imx-mkimage build
CFLAGS = "-O2 -Wall -std=c99 -I ${STAGING_INCDIR_NATIVE} -L ${STAGING_LIBDIR_NATIVE}"

# This package aggregates output deployed by other packages,
# so set the appropriate dependencies
do_compile[depends] += " \
    virtual/bootloader:do_deploy \
    ${@' '.join('%s:do_deploy' % r for r in '${IMX_EXTRA_FIRMWARE}'.split() )} \
    imx-atf:do_deploy \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os:do_deploy', '', d)} \
"

SC_FIRMWARE_NAME ?= "scfw_tcm.bin"

OEI_ENABLE = "${@bb.utils.contains('DEPENDS', 'virtual/imx-oei', 'YES', 'NO', d)}"
OEI_NAME ?= "oei-${OEI_CORE}-*.bin"

ATF_MACHINE_NAME ?= "bl31-${ATF_PLATFORM}.bin"
ATF_MACHINE_NAME:append = "${@bb.utils.contains('MACHINE_FEATURES', 'optee', '-optee', '', d)}"

BOOT_VARIANT ?= ""

TOOLS_NAME ?= "mkimage_imx8"

IMX_BOOT_SOC_TARGET       ?= "INVALID"

DEPLOY_OPTEE = "${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'true', 'false', d)}"
DEPLOY_OPTEE_STMM = "${@bb.utils.contains('MACHINE_FEATURES', 'optee stmm', 'true', 'false', d)}"

IMXBOOT_TARGETS ?= \
    "${@bb.utils.contains('UBOOT_CONFIG', 'fspi', 'flash_flexspi', \
        bb.utils.contains('UBOOT_CONFIG', 'nand', 'flash_nand', \
                                                  'flash_multi_cores flash_dcd', d), d)}"

BOOT_STAGING       = "${S}/${IMX_BOOT_SOC_TARGET}"
BOOT_STAGING:mx8m-generic-bsp  = "${S}/iMX8M"
BOOT_STAGING:mx8dx-generic-bsp = "${S}/iMX8QX"
BOOT_STAGING:mx91-generic-bsp  = "${S}/iMX91"
BOOT_STAGING:mx93-generic-bsp  = "${S}/iMX93"
BOOT_STAGING:mx95-generic-bsp  = "${S}/iMX95"

SOC_FAMILY                    = "INVALID"
SOC_FAMILY:mx8-generic-bsp    = "mx8"
SOC_FAMILY:mx8m-generic-bsp   = "mx8m"
SOC_FAMILY:mx8x-generic-bsp   = "mx8x"
SOC_FAMILY:mx8ulp-generic-bsp = "mx8ulp"
SOC_FAMILY:mx91-generic-bsp   = "mx91"
SOC_FAMILY:mx93-generic-bsp   = "mx93"
SOC_FAMILY:mx95-generic-bsp   = "mx95"

REV_OPTION ?= "REV=${IMX_SOC_REV_UPPER}"

UBOOT_DTB_BINARY ?= "u-boot.dtb"
MKIMAGE_EXTRA_ARGS ?= ""
MKIMAGE_EXTRA_ARGS:mx95-nxp-bsp ?= " \
    OEI=${OEI_ENABLE} \
    LPDDR_TYPE=${DDR_TYPE} \
    ${@'LPDDR_FW_VERSION='+d.getVar('LPDDR_FW_VERSION') if d.getVar('LPDDR_FW_VERSION') else ''} \
    ${@bb.utils.contains('SYSTEM_MANAGER_CONFIG', 'mx95alt', 'MSEL=1', '', d)}"
MKIMAGE_EXTRA_ARGS:imx95-19x19-verdin ?= " \
    ${MKIMAGE_EXTRA_ARGS:mx95-nxp-bsp} \
    QSPI_HEADER=./scripts/fspi_header_133"

UBOOT_DTB_BINARY ?= "u-boot.dtb"

compile_mx8m() {
    bbnote 8MQ/8MM/8MN/8MP boot binary build
    for ddr_firmware in ${DDR_FIRMWARE_NAME}; do
        bbnote "Copy ddr_firmware: ${ddr_firmware} from ${DEPLOY_DIR_IMAGE} -> ${BOOT_STAGING} "
        cp ${DEPLOY_DIR_IMAGE}/${ddr_firmware}               ${BOOT_STAGING}
    done

    cp ${DEPLOY_DIR_IMAGE}/signed_dp_imx8m.bin               ${BOOT_STAGING}
    cp ${DEPLOY_DIR_IMAGE}/signed_hdmi_imx8m.bin             ${BOOT_STAGING}
    cp ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG_EXTRA} \
                                                             ${BOOT_STAGING}/u-boot-spl.bin

    if [ "x${UBOOT_SIGN_ENABLE}" = "x1" ] ; then
        # Use DTB binary patched with signature node
        cp ${DEPLOY_DIR_IMAGE}/${UBOOT_DTB_BINARY}           ${BOOT_STAGING}/${UBOOT_DTB_NAME_EXTRA}
    else
        cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${UBOOT_DTB_NAME_EXTRA} \
                                                             ${BOOT_STAGING}
    fi
    if [ "${UBOOT_DTB_NAME_EXTRA}" != "${UBOOT_DTB_NAME}" ] ; then
        ln -sf ${UBOOT_DTB_NAME_EXTRA}                       ${BOOT_STAGING}/${UBOOT_DTB_NAME}
    fi

    cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/u-boot-nodtb.bin-${MACHINE}-${UBOOT_CONFIG_EXTRA} \
                                                             ${BOOT_STAGING}/u-boot-nodtb.bin

    cp ${DEPLOY_DIR_IMAGE}/${ATF_MACHINE_NAME}               ${BOOT_STAGING}/bl31.bin

    cp ${DEPLOY_DIR_IMAGE}/${UBOOT_NAME_EXTRA}               ${BOOT_STAGING}/u-boot.bin

}

compile_mx8() {
    bbnote 8QM boot binary build
    cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${SC_FIRMWARE_NAME} ${BOOT_STAGING}/scfw_tcm.bin
    cp ${DEPLOY_DIR_IMAGE}/${ATF_MACHINE_NAME}               ${BOOT_STAGING}/bl31.bin
    cp ${DEPLOY_DIR_IMAGE}/${UBOOT_NAME_EXTRA}                     ${BOOT_STAGING}/u-boot.bin
    cp ${DEPLOY_DIR_IMAGE}/${SECO_FIRMWARE_NAME}             ${BOOT_STAGING}
    if [ -e ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG_EXTRA} ] ; then
        cp ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG_EXTRA} \
                                                             ${BOOT_STAGING}/u-boot-spl.bin
    fi
}

compile_mx8x() {
    bbnote 8QX boot binary build
    cp ${DEPLOY_DIR_IMAGE}/${SECO_FIRMWARE_NAME}             ${BOOT_STAGING}
    cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${SC_FIRMWARE_NAME} ${BOOT_STAGING}/scfw_tcm.bin
    cp ${DEPLOY_DIR_IMAGE}/${ATF_MACHINE_NAME}               ${BOOT_STAGING}/bl31.bin
    cp ${DEPLOY_DIR_IMAGE}/${UBOOT_NAME_EXTRA}                     ${BOOT_STAGING}/u-boot.bin
    if [ -e ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG_EXTRA} ] ; then
        cp ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG_EXTRA} \
                                                             ${BOOT_STAGING}/u-boot-spl.bin
    fi
}

compile_mx8ulp() {
    bbnote 8ULP boot binary build
    cp ${DEPLOY_DIR_IMAGE}/${SECO_FIRMWARE_NAME}             ${BOOT_STAGING}/
    cp ${DEPLOY_DIR_IMAGE}/${ATF_MACHINE_NAME} ${BOOT_STAGING}/bl31.bin
    cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/upower.bin          ${BOOT_STAGING}/upower.bin
    cp ${DEPLOY_DIR_IMAGE}/${UBOOT_NAME_EXTRA}                     ${BOOT_STAGING}/u-boot.bin
    if [ -e ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG_EXTRA} ] ; then
        cp ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG_EXTRA} \
                                                             ${BOOT_STAGING}/u-boot-spl.bin
    fi
}

compile_mx91() {
    bbnote i.MX 91 boot binary build
    compile_mx93
}

compile_mx93() {
    bbnote i.MX 93 boot binary build
    for ddr_firmware in ${DDR_FIRMWARE_NAME}; do
        bbnote "Copy ddr_firmware: ${ddr_firmware} from ${DEPLOY_DIR_IMAGE} -> ${BOOT_STAGING} "
        cp ${DEPLOY_DIR_IMAGE}/${ddr_firmware}               ${BOOT_STAGING}
    done

    cp ${DEPLOY_DIR_IMAGE}/${SECO_FIRMWARE_NAME}             ${BOOT_STAGING}/
    cp ${DEPLOY_DIR_IMAGE}/${ATF_MACHINE_NAME} ${BOOT_STAGING}/bl31.bin
    cp ${DEPLOY_DIR_IMAGE}/${UBOOT_NAME_EXTRA}                     ${BOOT_STAGING}/u-boot.bin
    if [ -e ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG_EXTRA} ] ; then
        cp ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG_EXTRA} \
                                                             ${BOOT_STAGING}/u-boot-spl.bin
    fi
}

compile_mx95() {
    bbnote i.MX 95 boot binary build
    compile_mx93

    cp ${DEPLOY_DIR_IMAGE}/${SYSTEM_MANAGER_FIRMWARE_NAME}.bin \
       ${BOOT_STAGING}/${SYSTEM_MANAGER_FIRMWARE_BASENAME}.bin
}

do_compile() {
    # mkimage for i.MX8
    # Copy TEE binary to SoC target folder to mkimage
    if ${DEPLOY_OPTEE}; then
        cp ${DEPLOY_DIR_IMAGE}/tee.bin ${BOOT_STAGING}
        if ${DEPLOY_OPTEE_STMM}; then
            # Copy tee.bin to tee.bin-stmm
            cp ${DEPLOY_DIR_IMAGE}/tee.bin ${BOOT_STAGING}/tee.bin-stmm
        fi
    fi
    # Copy OEI firmware to SoC target folder to mkimage
    if [ "${OEI_ENABLE}" = "YES" ]; then
        cp ${DEPLOY_DIR_IMAGE}/${OEI_NAME} ${BOOT_STAGING}
    fi

    for type in ${UBOOT_CONFIG}; do
        if [ "${@d.getVarFlags('UBOOT_DTB_NAME')}" = "None" ]; then
            UBOOT_DTB_NAME_FLAGS="${type}:${UBOOT_DTB_NAME}"
        else
            UBOOT_DTB_NAME_FLAGS="${@' '.join(flag + ':' + dtb for flag, dtb in (d.getVarFlags('UBOOT_DTB_NAME')).items()) if d.getVarFlags('UBOOT_DTB_NAME') is not None else '' }"
        fi

        for key_value in ${UBOOT_DTB_NAME_FLAGS}; do
            type_key="${key_value%%:*}"
            dtb_name="${key_value#*:}"

            if [ "$type_key" = "$type" ]
            then
                bbnote "UBOOT_CONFIG = $type, UBOOT_DTB_NAME = $dtb_name"

                UBOOT_CONFIG_EXTRA="$type_key"
                if [ -e ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${dtb_name}-${type} ] ; then
                    UBOOT_DTB_NAME_EXTRA="${dtb_name}-${type}"
                else
                    # backward compatibility
                    UBOOT_DTB_NAME_EXTRA="${dtb_name}"
                fi
                UBOOT_NAME_EXTRA="u-boot-${MACHINE}.bin-${UBOOT_CONFIG_EXTRA}"
                BOOT_CONFIG_MACHINE_EXTRA="imx-boot${BOOT_VARIANT}-${MACHINE}-${UBOOT_CONFIG_EXTRA}.bin"

                for target in ${IMXBOOT_TARGETS}; do
                    compile_${SOC_FAMILY}
                    case $target in
                    *no_v2x)
                        # Special target build for i.MX 8DXL with V2X off
                        bbnote "building ${IMX_BOOT_SOC_TARGET} - ${REV_OPTION} V2X=NO ${target}"
                        make SOC=${IMX_BOOT_SOC_TARGET} ${REV_OPTION} V2X=NO dtbs=${UBOOT_DTB_NAME_EXTRA} flash_linux_m4
                        ;;
                    *stmm_capsule)
                        # target for flash_evk_stmm_capsule or
                        # flash_singleboot_stmm_capsule
                        cp ${RECIPE_SYSROOT_NATIVE}/${bindir}/mkeficapsule ${BOOT_STAGING}
                        bbnote "building ${IMX_BOOT_SOC_TARGET} - TEE=tee.bin-stmm ${target}"
                        cp ${DEPLOY_DIR_IMAGE}/CRT.* ${BOOT_STAGING}
                        make SOC=${IMX_BOOT_SOC_TARGET} TEE=tee.bin-stmm dtbs=${UBOOT_DTB_NAME} ${REV_OPTION} ${target}
                        ;;
                    *)
                        bbnote "building ${IMX_BOOT_SOC_TARGET} - ${REV_OPTION} ${MKIMAGE_EXTRA_ARGS} ${target}"
                        make SOC=${IMX_BOOT_SOC_TARGET} ${REV_OPTION} ${MKIMAGE_EXTRA_ARGS} dtbs=${UBOOT_DTB_NAME} ${target}
                        ;;
                    esac

                    if [ -e "${BOOT_STAGING}/flash.bin" ]; then
                        cp ${BOOT_STAGING}/flash.bin ${S}/${BOOT_CONFIG_MACHINE_EXTRA}-${target}
                    fi
                done

                unset UBOOT_CONFIG_EXTRA
                unset UBOOT_DTB_NAME_EXTRA
                unset UBOOT_NAME_EXTRA
                unset BOOT_CONFIG_MACHINE_EXTRA
            fi

            unset type_key
            unset dtb_name
        done

        unset UBOOT_DTB_NAME_FLAGS
    done
    unset type
}

do_install () {
    install -d ${D}/boot
    for type in ${UBOOT_CONFIG}; do

        bbnote "UBOOT_CONFIG = $type"

        UBOOT_CONFIG_EXTRA="$type"
        BOOT_CONFIG_MACHINE_EXTRA="imx-boot${BOOT_VARIANT}-${MACHINE}-${UBOOT_CONFIG_EXTRA}.bin"

        for target in ${IMXBOOT_TARGETS}; do
            install -m 0644 ${S}/${BOOT_CONFIG_MACHINE_EXTRA}-${target} ${D}/boot/
        done

        unset UBOOT_CONFIG_EXTRA
        unset BOOT_CONFIG_MACHINE_EXTRA
    done

    unset type
}

deploy_mx8m() {
    install -d ${DEPLOYDIR}/${BOOT_TOOLS}
    for ddr_firmware in ${DDR_FIRMWARE_NAME}; do
        install -m 0644 ${DEPLOY_DIR_IMAGE}/${ddr_firmware}  ${DEPLOYDIR}/${BOOT_TOOLS}
    done

    install -m 0644 ${BOOT_STAGING}/signed_dp_imx8m.bin      ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0644 ${BOOT_STAGING}/signed_hdmi_imx8m.bin    ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0755 ${BOOT_STAGING}/${TOOLS_NAME}            ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0755 ${BOOT_STAGING}/mkimage_fit_atf.sh       ${DEPLOYDIR}/${BOOT_TOOLS}
}

deploy_mx8() {
    install -d ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0644 ${BOOT_STAGING}/${SECO_FIRMWARE_NAME}    ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0755 ${S}/${TOOLS_NAME}                       ${DEPLOYDIR}/${BOOT_TOOLS}
    if [ -e ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG_EXTRA} ] ; then
        install -m 0644 ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG_EXTRA} \
                                                             ${DEPLOYDIR}/${BOOT_TOOLS}
    fi
}

deploy_mx8x() {
    install -d ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0644 ${BOOT_STAGING}/${SECO_FIRMWARE_NAME}    ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0755 ${S}/${TOOLS_NAME}                       ${DEPLOYDIR}/${BOOT_TOOLS}
    if [ -e ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG} ] ; then
        install -m 0644 ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG} \
                                                             ${DEPLOYDIR}/${BOOT_TOOLS}
    fi
}

deploy_mx8ulp() {
    install -d ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0644 ${BOOT_STAGING}/${SECO_FIRMWARE_NAME}    ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0755 ${S}/${TOOLS_NAME}                       ${DEPLOYDIR}/${BOOT_TOOLS}
    if [ -e ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG_EXTRA} ] ; then
        install -m 0644 ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG} \
                                                             ${DEPLOYDIR}/${BOOT_TOOLS}
    fi
}

deploy_mx91() {
    deploy_mx93
}

deploy_mx93() {
    install -d ${DEPLOYDIR}/${BOOT_TOOLS}

    for ddr_firmware in ${DDR_FIRMWARE_NAME}; do
        install -m 0644 ${DEPLOY_DIR_IMAGE}/${ddr_firmware}  ${DEPLOYDIR}/${BOOT_TOOLS}
    done

    install -m 0644 ${BOOT_STAGING}/${SECO_FIRMWARE_NAME}    ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0755 ${S}/${TOOLS_NAME}                       ${DEPLOYDIR}/${BOOT_TOOLS}
    if [ -e ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG} ] ; then
        install -m 0644 ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG} \
                                                             ${DEPLOYDIR}/${BOOT_TOOLS}
    fi
}

deploy_mx95() {
    deploy_mx93
    install -m 0644 ${BOOT_STAGING}/${SYSTEM_MANAGER_FIRMWARE_BASENAME}.bin \
                ${DEPLOYDIR}/${BOOT_TOOLS}/${SYSTEM_MANAGER_FIRMWARE_NAME}.bin
}

do_deploy() {
    deploy_${SOC_FAMILY}

    # copy tee.bin to deploy path
    if ${DEPLOY_OPTEE}; then
       install -m 0644 ${DEPLOY_DIR_IMAGE}/tee.bin ${DEPLOYDIR}/${BOOT_TOOLS}
    fi

    # copy oei to deploy path
    if [ "${OEI_ENABLE}" = "YES" ]; then
        install -m 0644 ${BOOT_STAGING}/${OEI_NAME} ${DEPLOYDIR}/${BOOT_TOOLS}
    fi

    # copy makefile (soc.mak) for reference
    install -m 0644 ${BOOT_STAGING}/soc.mak                  ${DEPLOYDIR}/${BOOT_TOOLS}

    # copy stmm files to deploy path
    if ${DEPLOY_OPTEE_STMM}; then
        install -m 0644 ${BOOT_STAGING}/tee.bin-stmm         ${DEPLOYDIR}/${BOOT_TOOLS}
        install -m 0644 ${BOOT_STAGING}/capsule1.bin         ${DEPLOYDIR}/${BOOT_TOOLS}
        install -m 0644 ${BOOT_STAGING}/CRT.*                ${DEPLOYDIR}/${BOOT_TOOLS}
        install -m 0755 ${BOOT_STAGING}/mkeficapsule         ${DEPLOYDIR}/${BOOT_TOOLS}
    fi

    for type in ${UBOOT_CONFIG}; do
        UBOOT_CONFIG_EXTRA="$type"
        UBOOT_NAME_EXTRA="u-boot-${MACHINE}.bin-${UBOOT_CONFIG_EXTRA}"
        BOOT_CONFIG_MACHINE_EXTRA="imx-boot${BOOT_VARIANT}-${MACHINE}-${UBOOT_CONFIG_EXTRA}.bin"

        if [ -e ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG_EXTRA} ] ; then
            install -m 0644 ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG_EXTRA} \
                                                             ${DEPLOYDIR}/${BOOT_TOOLS}
        fi
        # copy the tool mkimage to deploy path and sc fw, dcd and uboot
        install -m 0644 ${DEPLOY_DIR_IMAGE}/${UBOOT_NAME_EXTRA}  ${DEPLOYDIR}/${BOOT_TOOLS}

        # copy the generated boot image to deploy path
        for target in ${IMXBOOT_TARGETS}; do
            # Use first "target" as IMAGE_IMXBOOT_TARGET
            if [ "$IMAGE_IMXBOOT_TARGET" = "" ]; then
                IMAGE_IMXBOOT_TARGET="$target"
                echo "Set boot target as $IMAGE_IMXBOOT_TARGET"
            fi
            install -m 0644 ${S}/${BOOT_CONFIG_MACHINE_EXTRA}-${target} ${DEPLOYDIR}
        done

        # The first UBOOT_CONFIG listed will be the imx-boot binary
        if [ ! -f "${DEPLOYDIR}/imx-boot" ]; then
            ln -sf ${BOOT_CONFIG_MACHINE_EXTRA}-${IMAGE_IMXBOOT_TARGET} ${DEPLOYDIR}/imx-boot
        else
            bbwarn "Use custom wks.in for $UBOOT_CONFIG = $type"
        fi

        unset UBOOT_CONFIG_EXTRA
        unset UBOOT_NAME_EXTRA
        unset BOOT_CONFIG_MACHINE_EXTRA
    done
    unset type
}
addtask deploy before do_build after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"
FILES:${PN} = "/boot"

COMPATIBLE_MACHINE = "(mx8-generic-bsp|mx9-generic-bsp)"
