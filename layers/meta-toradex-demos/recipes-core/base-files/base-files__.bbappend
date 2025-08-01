# If hostname is empty, the /etc/hostname file is not created.
# Prevent creating /etc/hostname file which blocks starting the
# systemd service "set-hostname.service"
hostname = "${@bb.utils.contains('DISTRO_FEATURES','systemd','','${MACHINE}',d)}"
