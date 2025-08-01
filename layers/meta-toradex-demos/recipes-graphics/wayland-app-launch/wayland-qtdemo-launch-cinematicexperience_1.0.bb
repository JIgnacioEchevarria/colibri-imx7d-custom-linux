# set the following variable to your one and only application which should
# be launched right after weston started

INITIAL_APP_PKGS ?= "cinematicexperience qtwayland"
APPLICATION_ENVIRONMENT ?= 'QT_QPA_PLATFORM=wayland-egl'
WAYLAND_APPLICATION ?= "/usr/share/cinematicexperience-1.0/Qt5_CinematicExperience --fullscreen"

require wayland-app-launch.inc
