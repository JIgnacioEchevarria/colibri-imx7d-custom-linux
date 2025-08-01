# set the following variable to your one and only application which should
# be launched right after weston started

INITIAL_APP_PKGS ?= "qtsmarthome qtwayland"
APPLICATION_ENVIRONMENT ?= 'QT_QPA_PLATFORM=wayland-egl'
WAYLAND_APPLICATION ?= "/usr/share/qtsmarthome-1.0/smarthome"

require wayland-app-launch.inc
