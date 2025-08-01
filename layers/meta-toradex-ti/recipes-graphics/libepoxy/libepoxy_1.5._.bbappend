# Append EGL_CFLAGS to CFLAGS
CFLAGS:append:k3 = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', '', '-DEGL_API_FB', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', '-DWL_EGL_PLATFORM', '', d)} \
"
PACKAGE_ARCH:k3 = "${MACHINE_ARCH}"
