SRC_URI:append:aquila-am69 = " https://docs.toradex.com/116005-sx-pceax-board-files_v1.0.zip;name=sx-pceax-board-files \
                               https://docs.toradex.com/117028-sx-pcebe-board-files_v1.0.zip;name=sx-pcebe-board-files"
SRC_URI[sx-pceax-board-files.sha256sum] = "f94f6382ac1673b84a17e424b5bff681a108eea869544d9e3a3d804f498ae28f"
SRC_URI[sx-pcebe-board-files.sha256sum] = "a74d2acc8bab70c8cdd070f3dbbc3f5571b0eb1591f029ff4986ea47387d8d71"

ATH11K_FIRMWARE_PATH = "${nonarch_base_libdir}/firmware/ath11k/WCN6855/hw2.1"
ATH12K_FIRMWARE_PATH = "${nonarch_base_libdir}/firmware/ath12k/WCN7850/hw2.0"

do_install:append:aquila-am69 () {
	install -m 0644 ${WORKDIR}/sx-pceax-board-files/board-2_US_EU_JP.bin ${D}${ATH11K_FIRMWARE_PATH}
	install -m 0644 ${WORKDIR}/sx-pceax-board-files/board-2_UK_CA.bin    ${D}${ATH11K_FIRMWARE_PATH}
	install -m 0644 ${WORKDIR}/sx-pcebe-board-files/board-2.bin          ${D}${ATH12K_FIRMWARE_PATH}

	rm -f ${D}${ATH11K_FIRMWARE_PATH}/board-2.bin
	ln -frs ${D}${ATH11K_FIRMWARE_PATH}/board-2_US_EU_JP.bin ${D}${ATH11K_FIRMWARE_PATH}/board-2.bin
}
