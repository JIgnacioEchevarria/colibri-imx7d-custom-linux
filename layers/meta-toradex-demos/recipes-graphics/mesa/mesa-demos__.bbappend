FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:imxgpu = " file://0001-xeglgears-drop-usage-of-fooEXT-functions.patch"
PACKAGE_ARCH:imxgpu = "${MACHINE_SOCARCH}"
