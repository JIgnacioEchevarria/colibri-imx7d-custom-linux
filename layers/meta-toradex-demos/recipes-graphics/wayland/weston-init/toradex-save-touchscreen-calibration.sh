#!/bin/sh

if [ "$#" -ne 7 ]; then
	echo "This script is to be called by weston with new touch calibration data, it requires 7 parameters."
	exit 1
fi

echo 'ACTION=="add|change", SUBSYSTEM=="input", KERNEL=="event[0-9]*", ENV{ID_INPUT_TOUCHSCREEN}=="1", ENV{LIBINPUT_CALIBRATION_MATRIX}=''"'"$2 $3 $4 $5 $6 $7"'"' > /etc/udev/rules.d/libinput.rules

# Make udev process the new rule by triggering a "change" event:
udevadm trigger "$1"
