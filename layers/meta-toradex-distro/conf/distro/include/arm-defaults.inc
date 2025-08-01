# This function changes the default tune for machines which
# are based on armv7a or armv7ve to use common tune value, note
# that we enforce hard-float which is default on Linux microPlatform.
# If you have one of those machines which are armv7a or armv7ve but
# can't support hard-float, please change tune = 'armv7athf' to
# tune = 'armv7at' below but then this is for your own distro.

def at_least_one(elements, list):
    for i in list:
        if i in elements:
            return True
    return False

def arm_tune_handler(d):
    armv7athf_tune_features = ['armv7a', 'armv7ve', 'cortexa5', 'cortexa7', 'cortexa9', 'cortexa15']
    armv8a_tune_features = ['armv8a', 'crc']
    features = d.getVar('TUNE_FEATURES').split()
    if at_least_one(armv7athf_tune_features, features):
        tune = 'armv7athf'
        if 'bigendian' in features:
            tune += 'b'
        if 'vfpv3' in features:
            tune += '-vfpv3'
        if 'vfpv3d16' in features:
            tune += '-vfpv3d16'
        if 'neon' in features:
            tune += '-neon'
        if 'vfpv4' in features:
            tune += '-vfpv4'
    elif at_least_one(armv8a_tune_features, features):
        # all currently used i.MX 8 members support the following
        tune = 'armv8a-crypto'
    else:
        tune = d.getVar('DEFAULTTUNE')
    return tune

DEFAULTTUNE:tdx-distro := "${@arm_tune_handler(d)}"
