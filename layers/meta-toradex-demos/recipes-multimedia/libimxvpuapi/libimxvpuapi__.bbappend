# prevent installing in /usr/lib64/ on 64bit build hosts

EXTRA_OECONF += "--libdir=${libdir}"
