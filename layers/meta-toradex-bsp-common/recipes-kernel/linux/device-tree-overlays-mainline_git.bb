inherit toradex-devicetree

SRCBRANCH = "master"
SRCREV = "3946ce4f19c955077ab9edc96004808177901a22"
SRCREV:use-head-next = "${AUTOREV}"

COMPATIBLE_MACHINE:tdx = ".*"
COMPATIBLE_MACHINE = "^$"
