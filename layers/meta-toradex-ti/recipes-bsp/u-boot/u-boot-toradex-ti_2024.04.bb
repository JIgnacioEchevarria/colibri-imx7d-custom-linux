SUMMARY = "U-Boot bootloader with support for Toradex AM62 series SoMs"
HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"
LICENSE = "GPL-2.0-or-later"

require recipes-bsp/u-boot/u-boot-ti.inc

LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"

SRC_URI = "git://git.toradex.com/u-boot-toradex.git;protocol=https;branch=${SRCBRANCH}"

SRCREV = "edba3cda828cae94dfb1460dd071050190409cfa"
SRCREV:use-head-next = "${AUTOREV}"
SRCBRANCH = "toradex_ti-u-boot-2024.04"

CVE_PRODUCT = "denx:u-boot"
CVE_VERSION = "2024.04"

B = "${WORKDIR}/build"
S = "${WORKDIR}/git"

inherit toradex-u-boot-localversion

UBOOT_INITIAL_ENV = "u-boot-initial-env"

COMPATIBLE_MACHINE = "(ti-soc)"
PACKAGE_ARCH = "${MACHINE_ARCH}"

do_compile[mcdepends] += "mc::k3r5:virtual/bootloader:do_deploy"

do_compile:prepend:k3 () {
    cp ${DEPLOY_DIR_IMAGE}/tiboot3-*.bin ${STAGING_DIR_HOST}${nonarch_base_libdir}/firmware/
}

do_deploy:append:k3 () {
    if [ -n "${UBOOT_CONFIG}" ]
    then
        for config in ${UBOOT_MACHINE}; do
            for f in ${B}/${config}/firmware-*.bin; do
                if [ -f "$f" ]; then
                    install -m 644 $f ${DEPLOYDIR}/
                fi
            done
        done
    fi
}

do_deploy:append:k3r5 () {
    # remove any u-boot-initial-env* file not already removed by the
    # u-boot-ti.inc
    rm -f ${DEPLOYDIR}/u-boot-initial-env*

    if [ -n "${UBOOT_CONFIG}" ]
    then
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
                    for f in ${B}/${config}/tiboot3-*.bin; do
                        if [ -f "$f" ]; then
                            TARGET=$(basename $f)
                            install -m 644 $f ${DEPLOYDIR}/${TARGET}
                        fi
                    done

                    for f in ${B}/${config}/sysfw*.itb; do
                            if [ -f "$f" ]; then
                                    install -m 644 $f ${DEPLOYDIR}/
                            fi
                    done
                fi
            done
            unset j
        done
        unset i
    else
        if ! [ -f ${B}/${UBOOT_BINARY} ]; then
            ln -s spl/${UBOOT_BINARY} ${B}/${UBOOT_BINARY}
        fi
    fi
}

# build the k3r5 spl also for DFU
do_compile:append:k3r5 () {
    if [ -L ${B}/${UBOOT_BINARY} ]; then
        rm ${B}/${UBOOT_BINARY}
    fi

    if [ -n "${UBOOT_CONFIG}" ]
    then
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
                    if ! [ -f ${B}/${config}/${UBOOT_BINARY} ]; then
                        ln -s spl/${UBOOT_BINARY} ${B}/${config}/${UBOOT_BINARY}
                    fi
                fi
            done
            unset j
        done
        unset i
    else
        if ! [ -f ${B}/${UBOOT_BINARY} ]; then
            ln -s spl/${UBOOT_BINARY} ${B}/${UBOOT_BINARY}
        fi
    fi
}
