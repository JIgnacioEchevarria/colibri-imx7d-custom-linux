#!/usr/bin/env sh
#
# Called from udev. Compatible to colibri-imx6.
# Starting from v1.2 the STMPE811 is replaced by the TLA2024.
#
# Create/remove symlinks to/from the files with raw ain data.

# Map the ADC lines:
#  < v1.2: colibri-ain{0..3} -> STMPE811 ADC{4,5,6,7} in terms of the driver.
# >= v1.2: colibri-ain{0..3} ->  TLA2024 ADC{0,1,2,3} in terms of the driver.

LINES=4

# 4 for STMPE811, 0 for TLA2024
START=0
if [ "$(cat /sys$DEVPATH/name)" = "stmpe-adc" ]; then
    START=4
fi

if [ "$ACTION" = "add" ]; then
    for idx in `seq 0 $((LINES-1))`; do
        ln -s "/sys$DEVPATH/in_voltage$((START+idx))_raw" /dev/colibri-ain$idx
    done
elif [ "$ACTION" = "remove" ]; then
    for idx in `seq 0 $((LINES-1))`; do
        rm -f /dev/colibri-ain$idx
    done
fi

