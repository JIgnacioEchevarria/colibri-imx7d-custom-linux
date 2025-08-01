PACKAGECONFIG_EXAMPLES ?= "examples"

# | /build/krm/oe-core_V2.6/build/out-glibc/work/armv7at2hf-vfp-neon-mx6qdl-angstrom-linux-gnueabi/qtbase/5.5.1+gitAUTOINC+5afc431323-r0/git/src/widgets/styles/qgtkstyle.cpp: In member function 'virtual QRect QGtkStyle::subControlRect(QStyle::ComplexControl, const QStyleOptionComplex*, QStyle::SubControl, const QWidget*) const':
# | /build/krm/oe-core_V2.6/build/out-glibc/work/armv7at2hf-vfp-neon-mx6qdl-angstrom-linux-gnueabi/qtbase/5.5.1+gitAUTOINC+5afc431323-r0/git/src/widgets/styles/qgtkstyle.cpp:3636:24: error: 'isInstanceOf' is not a member of 'QStyleHelper'
# |              } else if (QStyleHelper::isInstanceOf(groupBox->styleObject, QAccessible::Grouping)) {
# |                         ^
# | /build/krm/oe-core_V2.6/build/out-glibc/work/armv7at2hf-vfp-neon-mx6qdl-angstrom-linux-gnueabi/qtbase/5.5.1+gitAUTOINC+5afc431323-r0/git/src/widgets/styles/qgtkstyle.cpp:3636:74: error: 'QAccessible' has not been declared
# |              } else if (QStyleHelper::isInstanceOf(groupBox->styleObject, QAccessible::Grouping)) {

PACKAGECONFIG:append = " accessibility"
PACKAGECONFIG:append = " sql-sqlite"

PACKAGECONFIG_FONTS:append = " fontconfig"

#qtbase must be configured with icu for qtwebkit
PACKAGECONFIG:append = " \
    icu \
    ${PACKAGECONFIG_EXAMPLES} \
"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

IMX_BACKEND = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', \
                  bb.utils.contains('DISTRO_FEATURES',     'x11',     'x11', \
                                                                       'fb', d), d)}"

SRC_URI:append = " \
    file://qt5-${IMX_BACKEND}.sh \
"

do_install:append () {
    install -d ${D}${sysconfdir}/profile.d/
    install -m 0755 ${WORKDIR}/qt5-${IMX_BACKEND}.sh ${D}${sysconfdir}/profile.d/

}

FILES:${PN} += "${sysconfdir}/profile.d/qt5*.sh"
