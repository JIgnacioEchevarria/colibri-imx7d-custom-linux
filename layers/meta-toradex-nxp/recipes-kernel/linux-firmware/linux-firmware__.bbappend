PV = "20250311"
WHENCE_CHKSUM = "886924eb733c4efcec21dff980795771"
SRC_URI[sha256sum] = "b1083a36f19aea46f661dcfd4cd462d13933dcb4e7f0dc809525552dd5c3541d"

PACKAGECONFIG[deduplicate] = ",,rdfind-native"

# Specifying -j requires GNU parallel, which is a part of meta-oe
PARALLEL_MAKE = ""

do_install() {
        sed -i 's:^./check_whence.py:#./check_whence.py:' ${S}/copy-firmware.sh

        oe_runmake 'DESTDIR=${D}' 'FIRMWAREDIR=${nonarch_base_libdir}/firmware' install
        cp LICEN[CS]E.* WHENCE ${D}${nonarch_base_libdir}/firmware/
}
