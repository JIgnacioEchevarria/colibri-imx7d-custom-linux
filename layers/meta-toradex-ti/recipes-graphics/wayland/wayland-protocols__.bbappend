FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:k3 = " \
    file://0001-wayland-drm-Add-wayland-drm-protocol-to-stable.patch \
    file://0002-wayland-drm-Update-to-version-2.patch \
"

do_install:append:k3 () {
	install -d ${D}${datadir}/wayland-protocols/stable/wayland-drm
	cp ${S}/stable/wayland-drm/wayland-drm.xml ${D}${datadir}/wayland-protocols/stable/wayland-drm/
}
