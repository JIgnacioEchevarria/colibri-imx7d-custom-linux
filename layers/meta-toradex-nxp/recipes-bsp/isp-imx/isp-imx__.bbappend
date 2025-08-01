FILESEXTRAPATHS:prepend := "${THISDIR}/isp-imx/:"
SRC_URI:append:tdx = " \
    file://0001-isp-imx-start_isp-don-t-report-error-if-no-camera-is.patch \
"
