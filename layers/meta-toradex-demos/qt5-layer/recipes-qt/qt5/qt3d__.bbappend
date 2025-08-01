FILESEXTRAPATHS:prepend := "${THISDIR}/qt3d:"

# qt3d links against the opengl flavours we configured qtbase for, so
# depend on them here
DEPENDS_GLES = ""
DEPENDS_GLES:imxpxp += "virtual/libgles2 virtual/egl"
DEPENDS_GLES_imgpu3d += "virtual/libgles2 virtual/egl"
DEPENDS_GLES:upstream += "virtual/libgles2 virtual/egl"

DEPENDS:class-target += " ${DEPENDS_GLES}"

# Fix race condition
SRC_URI += "file://0001-qt3d-do-not-set-resources_big.patch"
