FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# TI k3 extra gles2 packageconfig flags
GLES_EXTRA_DEPS:k3 = "libdrm wayland"
# rename from gles2 to gles2extra to preserve the original one for non k3 machines
PACKAGECONFIG[gles2extra] = "-opengl es2 -eglfs,,virtual/libgles2 virtual/egl ${GLES_EXTRA_DEPS}"

QT_CONFIG_FLAGS:append:k3 = " -qpa ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', 'eglfs', d)}"

QT_EGLFS_PATCHES = "\
    file://0001-calculator-Add-exit-button-for-non-window-environmen.patch \
    file://0002-animatedtiles-Add-exit-button-for-non-window-environ.patch \
    file://quit.png \
"

#    file://0002-deform-disable-opengl-button.patch

SRC_URI:append:k3 = "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', '', "${QT_EGLFS_PATCHES}", d)}\
    file://0001-deform-Fix-how-controls-are-shown.patch \
    file://0001-qtbase-plugins-platforms-eglfs_kms-fix-compiler-erro.patch \
    file://0001-eglfs-Force-888-format-only-on-env-flag.patch \
"

python do_patch:append:k3 () {
    import shutil

    work_dir = d.getVar("WORKDIR")
    s = d.getVar("S")

    if not bb.utils.contains('DISTRO_FEATURES','wayland',True,False,d):
        shutil.copy(os.path.join(work_dir,"quit.png"),os.path.join(s,"examples/widgets/animation/animatedtiles/images/"))
}

# Add symbolic link qt5/examples for backward compatibility
do_install:append:k3 () {

    install -d ${D}${datadir}/qt5
    ln -sf ../examples ${D}${datadir}/qt5/examples
}

FILES:${PN}-examples:append:k3 =  " ${datadir}/qt5/*"


RDEPENDS:${PN}:k3 += "${PN}-conf"

PACKAGE_ARCH:k3 = "${MACHINE_ARCH}"


############################### TDX added

# from meta-arago/meta-arago-distro/conf/distro/arago.conf
PACKAGECONFIG_GL:k3 = "gles2extra linuxfb"
PACKAGECONFIG_DISTRO:k3 = "icu examples accessibility gif gbm kms libinput xkbcommon"
PACKAGECONFIG_FONTS:k3 = "fontconfig"

PACKAGECONFIG:remove:k3 = "kms"
PACKAGECONFIG:remove:k3 = "vulkan"
PACKAGECONFIG:remove:k3 = "glib xcb"
