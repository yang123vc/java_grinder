#!/usr/bin/env bash

extra=""

if [ "$1" = "debug" ]
then
  extra="-debug"
fi

if [ "$1" = "avi" ]
then
  extra="-aviwrite out.avi"
fi

/usbdisk/source/mame/mess64 genesis -window \
  -cart1 sega_genesis_java_demo.bin \
  ${extra}

if [ "$1" = "avi" ]
then
  mencoder -oac mp3lame -ovc x264 -o video.avi -vf scale=640:480 ~/.mess/snap/out.avi
fi

