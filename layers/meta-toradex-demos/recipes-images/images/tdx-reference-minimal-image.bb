inherit core-image

SUMMARY = "Toradex Embedded Linux Reference Minimal Image"
DESCRIPTION = "Minimal image without graphical interface that just boots"

LICENSE = "MIT"

#Prefix to the resulting deployable tarball name
export IMAGE_BASENAME = "Reference-Minimal-Image"
MACHINE_NAME ?= "${MACHINE}"
IMAGE_NAME = "${MACHINE_NAME}_${IMAGE_BASENAME}"

# Copy Licenses to image /usr/share/common-license
COPY_LIC_MANIFEST ?= "1"
COPY_LIC_DIRS ?= "1"

add_rootfs_version () {
    printf "${DISTRO_NAME} ${DISTRO_VERSION} (${DISTRO_CODENAME}) \\\n \\\l\n" > ${IMAGE_ROOTFS}/etc/issue
    printf "${DISTRO_NAME} ${DISTRO_VERSION} (${DISTRO_CODENAME}) %%h\n" > ${IMAGE_ROOTFS}/etc/issue.net
    printf "${IMAGE_NAME}\n\n" >> ${IMAGE_ROOTFS}/etc/issue
    printf "${IMAGE_NAME}\n\n" >> ${IMAGE_ROOTFS}/etc/issue.net
}

add_home_root_symlink () {
    ln -sf ${ROOT_HOME} ${IMAGE_ROOTFS}/home/root
}

# add the rootfs version to the welcome banner
ROOTFS_POSTPROCESS_COMMAND += " add_rootfs_version; add_home_root_symlink;"

IMAGE_LINGUAS = "en-us"
#IMAGE_LINGUAS = "de-de fr-fr en-gb en-us pt-br es-es kn-in ml-in ta-in"

CONMANPKGS ?= "connman connman-plugin-loopback connman-plugin-ethernet connman-plugin-wifi connman-client"

IMAGE_INSTALL += " \
    packagegroup-boot \
    packagegroup-basic \
    packagegroup-base-tdx-cli \
    packagegroup-machine-tdx-cli \
    packagegroup-wifi-tdx-cli \
    packagegroup-wifi-fw-tdx-cli \
    udev-extraconf \
    ${CONMANPKGS} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'timestamp-service systemd-analyze', '', d)} \
"
