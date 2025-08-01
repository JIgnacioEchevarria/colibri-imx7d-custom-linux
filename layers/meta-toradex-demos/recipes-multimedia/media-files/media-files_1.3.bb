SUMMARY = "Media Files for tests"
LICENSE = "CC0-1.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/CC0-1.0;md5=0ceb3372c9595f0a8067e55da801e4a1"

inherit allarch bin_package

SRC_URI = "https://docs1.toradex.com/114780-media-files-${PV}.tar.xz"

SRC_URI[sha256sum] = "d6a3cd2003798fec80fb8008d2e48a5fa2c581f4ae66c03cd573d33b18341e67"

S = "${WORKDIR}/media-files"

# Install the files to ${D}${ROOT_HOME}
# Source code of original poky function:
# https://git.yoctoproject.org/poky/plain/meta/classes-recipe/bin_package.bbclass
# The function is being modified to install it inside ROOT_HOME, since we want
# these files inside the root directory and ROOT_HOME can change.
do_install () {
    # Do it carefully
    [ -d "${S}" ] || exit 1
    if [ -z "$(ls -A ${S})" ]; then
        bbfatal bin_package has nothing to install. Be sure the SRC_URI unpacks into S.
    fi
    cd ${S}
    install -d ${D}${base_prefix}${ROOT_HOME}
    tar --no-same-owner --exclude='./patches' --exclude='./.pc' -cpf - . \
        | tar --no-same-owner -xpf - -C ${D}${base_prefix}${ROOT_HOME}
}
