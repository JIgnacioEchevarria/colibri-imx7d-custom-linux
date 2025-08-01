# Fails to build for ARM, at least with angstrom and the linaro toolchain
# | error: matchbox-keyboard-image.o: requires unsupported dynamic reloc R_ARM_THM_MOVW_ABS_NC; recompile with -fPIC
LDFLAGS += " ${@bb.utils.contains('DISTRO_FEATURES', 'ld-is-gold', ' -fuse-ld=bfd ', '', d)}"
