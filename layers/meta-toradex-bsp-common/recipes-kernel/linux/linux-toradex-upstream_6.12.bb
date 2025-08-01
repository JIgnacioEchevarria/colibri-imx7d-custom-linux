require linux-toradex-upstream.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/linux-toradex-upstream-6.12:"

LINUX_REPO = "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux.git"

TDX_PATCHES = " \
    file://0001-thermal-imx-Update-critical-temp-threshold.patch \
    file://0002-Revert-drm-panel-simple-drop-use-of-data-mapping-pro.patch \
    file://0004-media-i2c-ov5640-Implement-get_mbus_config.patch \
    file://0001-usb-gadget-f_ncm-Apply-workaround-for-packet-cloggin.patch \
    file://0001-i2c-imx-do-not-poll-for-bus-busy-in-single-master-mo.patch \
    file://0002-i2c-imx-separate-atomic-dma-and-non-dma-use-case.patch \
    file://0003-i2c-imx-prevent-rescheduling-in-non-dma-mode.patch \
    file://0004-arm64-dts-imx8mm-verdin-add-single-master-property-t.patch \
    file://0005-arm64-dts-imx8mp-verdin-add-single-master-property-t.patch \
    file://0001-i2c-imx-fix-missing-stop-condition-in-single-master-.patch \
    file://0001-arm64-dts-imx8mp-Fix-VPU-PGC-power-domain-parents.patch \
    file://0001-arm64-dts-ti-k3-am62-verdin-dahlia-add-Microphone-Ja.patch \
    file://0001-dt-bindings-arm-freescale-Add-verdin-imx8mp-ivy-boar.patch \
    file://0002-arm64-dts-freescale-imx8mp-verdin-add-labels-to-som-.patch \
    file://0003-arm64-dts-freescale-imx8mp-verdin-Add-Ivy-carrier-bo.patch \
    file://0004-arm64-dts-ti-k3-am62-verdin-Fix-SoM-ADC-compatible.patch \
    file://0005-dt-bindings-arm-ti-Add-verdin-am62-ivy-board.patch \
    file://0006-arm64-dts-ti-k3-am62-verdin-add-label-to-som-adc-nod.patch \
    file://0007-arm64-dts-ti-k3-am62-verdin-Add-Ivy-carrier-board.patch \
    file://0001-i2c-imx-fix-emulated-smbus-block-read.patch \
    file://0001-wifi-mwifiex-enable-host-mlme-on-sdio-W8997-chipsets.patch \
    file://0001-dt-bindings-arm-fsl-add-Toradex-SMARC-iMX8MP-SoM-and.patch \
    file://0002-arm64-dts-freescale-add-Toradex-SMARC-iMX8MP.patch \
    file://0003-dt-bindings-power-reset-add-toradex-smarc-ec.patch \
    file://0004-power-reset-add-Toradex-Embedded-Controller.patch \
    file://0005-arm64-dts-freescale-imx8mp-toradex-smarc-add-fan-PWM.patch \
    file://0006-arm64-dts-freescale-imx8mp-toradex-smarc-add-embedde.patch \
    file://0007-arm64-dts-freescale-imx8mp-toradex-smarc-add-gpio-ex.patch \
    file://0008-arm64-dts-freescale-imx8mp-toradex-smarc-use-generic.patch \
"

PV = "6.12"
LINUX_VERSION ?= "6.12.35"
LINUX_KERNEL_TYPE:preempt-rt = "preempt-rt"
KBRANCH = "linux-6.12.y"
KERNEL_VERSION_SANITY_SKIP = "1"
SRCREV_machine = "783cd2c3dca8b6c434e955b84c20c8940588dc68"
SRCREV_machine:use-head-next = "${AUTOREV}"
