# This allows for udevd automounting with mounts accessible to all.
do_configure:prepend:tdx-distro () {
    sed -i '/PrivateMounts=yes/d' ${S}/units/systemd-udevd.service.in
}
