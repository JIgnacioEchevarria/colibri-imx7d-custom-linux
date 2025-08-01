SUMMARY = "Packagegroups which provide graphical/display/multimedia releated packages"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PROVIDES = "${PACKAGES}"
PACKAGES += " \
    packagegroup-drm-utils-tdx-graphical \
    packagegroup-gstreamer-tdx-graphical \
    packagegroup-gpu-tdx-graphical \
    packagegroup-vulkan-tools-tdx-graphical \
"

RRECOMMENDS:packagegroup-tdx-graphical = " \
    packagegroup-drm-utils-tdx-graphical \
    packagegroup-gstreamer-tdx-graphical \
    packagegroup-gpu-tdx-graphical \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vulkan', \
                         'packagegroup-vulkan-tools-tdx-graphical', '', d)} \
"

SUMMARY:packagegroup-drm-utils-tdx-graphical = "Utilities for DRM, Direct Rendering Manager"
RRECOMMENDS:packagegroup-drm-utils-tdx-graphical = " \
    libdrm-tests \
"

SUMMARY:packagegroup-gstreamer-tdx-graphical = "gstreamer packages"
RRECOMMENDS:packagegroup-gstreamer-tdx-graphical = " \
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-good \
    gstreamer1.0-plugins-bad \
    gst-examples \
"
RRECOMMENDS:packagegroup-gstreamer-tdx-graphical:colibri-imx6ull = ""
RRECOMMENDS:packagegroup-gstreamer-tdx-graphical:colibri-imx6ull-emmc = ""
RRECOMMENDS:packagegroup-gstreamer-tdx-graphical:append:mx8-nxp-bsp = " \
    imx-gst1.0-plugin \
    imx-gst1.0-plugin-gplay \
    imx-gst1.0-plugin-grecorder \
    packagegroup-fsl-gstreamer1.0-full \
"

SUMMARY:packagegroup-gpu-utils-tdx-graphical = "Utilities for GPU (OpenGL...)"
IMAGE_INSTALL_GPU_IMX_DRIVERS:mx8-nxp-bsp = " \
    tinycompress \
    libvdk-imx \
"
IMAGE_INSTALL_GPU_IMX_DRIVERS:mx95-nxp-bsp = " \
    mali-imx-opencl-icd \
    mali-imx-libvulkan \
"
IMAGE_INSTALL_GPU_IMX:mx8mm-nxp-bsp = " \
    ${IMAGE_INSTALL_GPU_IMX_DRIVERS} \
"
IMAGE_INSTALL_GPU_IMX = " \
    ${IMAGE_INSTALL_GPU_IMX_DRIVERS} \
    vulkan-headers \
    vulkan-loader \
    vulkan-tools \
    clpeak \
"
RRECOMMENDS:packagegroup-gpu-tdx-graphical = " \
    glmark2 \
"
RRECOMMENDS:packagegroup-gpu-tdx-graphical:colibri-imx6ull = ""
RRECOMMENDS:packagegroup-gpu-tdx-graphical:colibri-imx6ull-emmc = ""
RRECOMMENDS:packagegroup-gpu-tdx-graphical:append:mx8-nxp-bsp = " \
    ${IMAGE_INSTALL_GPU_IMX} \
"
RRECOMMENDS:packagegroup-gpu-tdx-graphical:append:mx95-nxp-bsp = " \
    ${IMAGE_INSTALL_GPU_IMX} \
"
RRECOMMENDS:packagegroup-gpu-tdx-graphical:append:mx8qm-nxp-bsp = " \
    libopenvx-imx \
"

SUMMARY:packagegroup-vulkan-tools-tdx-graphical = "Vulkan tools"
RRECOMMENDS:packagegroup-vulkan-tools-tdx-graphical = " \
    vulkan-tools \
"
