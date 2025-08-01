# Copyright (C) 2016 Toradex AG
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Deploys Vivante GPU libraries, even if no package RDEPENDS on them"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} = " \
    libgal-imx \
    libegl-imx \
    libgl-imx \
    libgles-imx \
    libgles2-imx \
    libopenvg-imx \
    libvdk-imx \
"
