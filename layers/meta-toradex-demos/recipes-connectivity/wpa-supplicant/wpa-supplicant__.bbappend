do_install:append () {
    # Disable background scan to avoid significant increase in bandwidth
    sed -i '/update_config/abgscan=""' ${D}${sysconfdir}/wpa_supplicant.conf
}
