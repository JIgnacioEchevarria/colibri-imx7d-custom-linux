PACKAGES =+ "${PN}-scp-dev ${PN}-sftp-dev ${PN}-sftp-server-dev"
PACKAGES =+ "${PN}-scp-dbg ${PN}-sftp-dbg ${PN}-sftp-server-dbg"

FILES:${PN}-scp-dev = ""
FILES:${PN}-sftp-dev = ""
FILES:${PN}-sftp-server-dev = ""
FILES:${PN}-scp-dbg = "${bindir}/.debug/scp.${BPN}"
FILES:${PN}-sftp-dbg = "${bindir}/.debug/sftp"
FILES:${PN}-sftp-server-dbg = "${libexecdir}/.debug/sftp-server"

#do not use reverse DNS
do_install:append () {
    sed -i -e 's:^#UseDNS.*$:UseDNS no:g' ${D}${sysconfdir}/ssh/sshd_config
}
