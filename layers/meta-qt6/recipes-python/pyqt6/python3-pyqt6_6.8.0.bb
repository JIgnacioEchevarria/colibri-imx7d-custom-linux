SUMMARY = "Python Qt6 Bindings"
HOMEPAGE = "https://www.riverbankcomputing.com/software/pyqt"
SECTION = "devel/python"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d32239bcb673463ab874e80d47fae504"

inherit pypi python3targetconfig python3-dir qt6-qmake qt6-paths

PYPI_PACKAGE = "PyQt6"

SRC_URI[sha256sum] = "6d8628de4c2a050f0b74462e4c9cb97f839bf6ffabbca91711722ffb281570d9"
SRC_URI += "file://0001-Fix-build-with-Qt-6.8.2.patch"

S = "${WORKDIR}/PyQt6-${PV}"
B = "${S}/build"

DEPENDS += " \
    qtbase \
    qtdeclarative \
    sip (>= 6.7.12) \
    sip-native (>= 6.7.12) \
    python3 \
    python3-pyqt6-sip-native \
    python3-pyqt6-sip \
    python3-packaging-native \
    python3-tomli-native \
    python3-pyqt-builder-native \
    python3-ply \
    python3-ply-native \
"

RDEPENDS:${PN} += " \
    qtbase \
    qtdeclarative \
    python3-core \
    python3-pyqt6-sip \
"

# Disable support of 128bit ints and add path to Python.h
CXXFLAGS += " -DQT_NO_INT128 -I${PYTHON_INCLUDE_DIR}"

EXTRA_OEMAKE += "INSTALL_ROOT=${D}"

DISABLED_FEATURES = " \
    PyQt_Desktop_OpenGL \
    PyQt_Accessibility \
    PyQt_SessionManager \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', '', 'PyQt_OpenGL', d)} \
"

PYQT_MODULES = " \
    QtCore \
    QtGui \
    QtNetwork \
    QtXml \
    QtNetwork \
    QtQml \
    QtSql \
"

do_configure() {
    extra_args=""

    for i in ${DISABLED_FEATURES}; do
        extra_args="$extra_args --disabled-feature=$i"
    done

    for i in ${PYQT_MODULES}; do
        extra_args="$extra_args --enable=$i"
    done

    cd ${S}
    sip-build \
        --verbose \
        --confirm-license \
        --scripts-dir="${bindir}" \
        --build-dir="${B}" \
        --target-dir="${PYTHON_SITEPACKAGES_DIR}" \
        --no-make \
        --qmake=${OE_QMAKE_QMAKE} \
        --pep484-pyi \
        --no-dbus-python \
        $extra_args

    QMAKE_PROFILES=${B}/PyQt6.pro

    # Fixes: cc1plus: error: include location "/usr/include/python3.13"
    # is unsafe for cross-compilation [-Werror=poison-system-directories]
    sed -i "s|/usr/include/${PYTHON_DIR}|${PYTHON_INCLUDE_DIR}|g" ${B}/*/*.pro
}

do_compile:append() {
    sed -i "s,${STAGING_DIR_TARGET},," ${B}/inventory.txt
}

do_install:append() {
    sed -i "s,exec .*nativepython3,exec ${bindir}/python3," ${D}/${bindir}/*
}

# fix buildpaths warnings in python3-pyqt6-src
pyqt_fix_sources() {
    find ${PKGD}/usr/src/debug/${PN} -type f -exec sed -i "s,\(${B}\|${S}\),/usr/src/debug/${PN}/${PV}-${PR},g" {} \;
}
PACKAGESPLITFUNCS:prepend = "pyqt_fix_sources"

# Ignore warnings about TMPDIR [buildpaths] in libpyqt6qmlplugin.so
INSANE_SKIP:${PN} += "buildpaths"
INSANE_SKIP:${PN}-dbg += "buildpaths"

FILES:${PN} += "${PYTHON_SITEPACKAGES_DIR} ${OE_QMAKE_PATH_PLUGINS}"
