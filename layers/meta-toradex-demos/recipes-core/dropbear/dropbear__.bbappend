# THE Eclipse RSE system explorer uses a ssh client which cannot cope with the
# dropbear ssh server if weak ciphers are disabled.
# If debug-tweaks is set in IMAGE_FEATURES then enable also weak ciphers.
# With debug-tweaks we allow password less root access, enforcing strong
# ciphers is pointless anyway.
PACKAGECONFIG:remove = "${@bb.utils.contains('IMAGE_FEATURES', 'debug-tweaks', 'disable-weak-ciphers', '',d)}"
