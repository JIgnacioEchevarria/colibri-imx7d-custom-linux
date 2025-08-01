SUMMARY = "Lightweight crypto and SSL/TLS library"
DESCRIPTION = "mbedtls is a lean open source crypto library          \
for providing SSL and TLS support in your programs. It offers        \
an intuitive API and documented header files, so you can actually    \
understand what the code does. It features:                          \
                                                                     \
 - Symmetric algorithms, like AES, Blowfish, Triple-DES, DES, ARC4,  \
   Camellia and XTEA                                                 \
 - Hash algorithms, like SHA-1, SHA-2, RIPEMD-160 and MD5            \
 - Entropy pool and random generators, like CTR-DRBG and HMAC-DRBG   \
 - Public key algorithms, like RSA, Elliptic Curves, Diffie-Hellman, \
   ECDSA and ECDH                                                    \
 - SSL v3 and TLS 1.0, 1.1 and 1.2                                   \
 - Abstraction layers for ciphers, hashes, public key operations,    \
   platform abstraction and threading                                \
"

HOMEPAGE = "https://tls.mbed.org/"

LICENSE = "Apache-2.0 | GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=379d5819937a6c2f1ef1630d341e026d"

SECTION = "libs"

S = "${WORKDIR}/git"
SRCREV = "2fc8413bfcb51354c8e679141b17b3f1a5942561"
SRC_URI = "git://github.com/Mbed-TLS/mbedtls.git;protocol=https;branch=mbedtls-2.28 \
           file://run-ptest \
          "

inherit cmake update-alternatives ptest

PACKAGECONFIG ??= "shared-libs programs ${@bb.utils.contains('PTEST_ENABLED', '1', 'tests', '', d)}"
PACKAGECONFIG[shared-libs] = "-DUSE_SHARED_MBEDTLS_LIBRARY=ON,-DUSE_SHARED_MBEDTLS_LIBRARY=OFF"
PACKAGECONFIG[programs] = "-DENABLE_PROGRAMS=ON,-DENABLE_PROGRAMS=OFF"
PACKAGECONFIG[werror] = "-DMBEDTLS_FATAL_WARNINGS=ON,-DMBEDTLS_FATAL_WARNINGS=OFF"
# Make X.509 and TLS calls use PSA
# https://github.com/Mbed-TLS/mbedtls/blob/development/docs/use-psa-crypto.md
PACKAGECONFIG[psa] = ""
PACKAGECONFIG[tests] = "-DENABLE_TESTING=ON,-DENABLE_TESTING=OFF"

EXTRA_OECMAKE = "-DLIB_INSTALL_DIR:STRING=${libdir}"

# For now the only way to enable PSA is to explicitly pass a -D via CFLAGS
CFLAGS:append = "${@bb.utils.contains('PACKAGECONFIG', 'psa', ' -DMBEDTLS_USE_PSA_CRYPTO', '', d)}"

PROVIDES += "polarssl"
RPROVIDES:${PN} = "polarssl"

PACKAGES =+ "${PN}-programs"
FILES:${PN}-programs = "${bindir}/"

ALTERNATIVE:${PN}-programs = "hello"
ALTERNATIVE_LINK_NAME[hello] = "${bindir}/hello"

BBCLASSEXTEND = "native nativesdk"

CVE_PRODUCT = "mbed_tls"

CVE_STATUS[CVE-2021-43666] = "backported-patch: Fix merged upstream https://github.com/Mbed-TLS/mbedtls/pull/5310"
CVE_STATUS[CVE-2021-45451] = "backported-patch: Fix merged upstream https://github.com/Mbed-TLS/mbedtls/commit/9a4a9c66a48edfe9ece03c7e4a53310adf73a86c"

# Strip host paths from autogenerated test files
do_compile:append() {
	sed -i 's+${S}/++g' ${B}/tests/*.c 2>/dev/null || :
	sed -i 's+${B}/++g' ${B}/tests/*.c 2>/dev/null || :
}

# Export source files/headers needed by Arm Trusted Firmware
sysroot_stage_all:append() {
	sysroot_stage_dir "${S}/library" "${SYSROOT_DESTDIR}/usr/share/mbedtls-source/library"
	sysroot_stage_dir "${S}/include" "${SYSROOT_DESTDIR}/usr/share/mbedtls-source/include"
}

do_install_ptest () {
	install -d ${D}${PTEST_PATH}/tests
	cp -f ${B}/tests/test_suite_* ${D}${PTEST_PATH}/tests/
	find ${D}${PTEST_PATH}/tests/ -type f -name "*.c" -delete
	cp -fR ${S}/tests/data_files ${D}${PTEST_PATH}/tests/
}
