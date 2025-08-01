# Recipe creation tool - update SRCREV
#
# Copyright (C) 2015 Intel Corporation
# Copyright (C) 2020 Toradex
#
# SPDX-License-Identifier: GPL-2.0-only
#
# Script which reads git hash from local BB_URI_HEADREVS cache and updates
# recipe files with it.
#
# Use export BB_SRCREV_POLICY="cache" before launching this tool to reuse git
# hashes from cache.
#

import sys
import os
import argparse
import glob
import fnmatch
import re
import logging
import scriptutils

logger = logging.getLogger('recipetool')

tinfoil = None
plugins = None

def tinfoil_init(instance):
    global tinfoil
    tinfoil = instance

def updatesrcrev(args):
    import oe.recipeutils

    rd = tinfoil.parse_recipe_file(args.recipefile, False)
    if not rd:
        return 1

    # The line `PV = "2020.07+git${SRCPV}"` in the U-Boot 2020.07 recipe
    # makes the uprev of that recipe fail if no MACHINE is defined.
    # (Because then UBOOT_MACHINE or UBOOT_CONFIG is not defined.)
    # Just reading PV of the datastore fixes the issue
    pv = rd.getVar('PV')

    src_uris = rd.getVar('SRC_URI').split()
    revision = {}
    for src_uri in src_uris:
        # Support git only
        if not src_uri.startswith(('git://', 'gitsm://')):
            continue

        # Workaround check in fetcher code, see bitbake commit 4b5eed16
        if not "AUTOINC" in pv:
            pkgpv = bb.fetch2.get_srcrev(rd, 'gitpkgv_revision')

        ud = bb.fetch2.FetchData(src_uri, rd)
        # Allow multiple "named" git repos
        for name in ud.names:
            revision[name] = ud.method.latest_revision(ud, rd, name)

    # Update SRC_URI variable by default
    varvalues = {}
    for name in revision.keys():
        filename = os.path.basename(rd.getVar('FILE', False))
        var = 'SRCREV' if name == 'default' else 'SRCREV_{}'.format(name)
        logger.info('{}: Update {} to {}.'.format(filename, var, revision[name]))
        varvalues[var] = revision[name]

    if len(varvalues) == 0:
        logger.error('No updatable revision found.')
        return 1

    patches = oe.recipeutils.patch_recipe(rd, args.recipefile, varvalues, patch=args.patch)
    if args.patch:
        for patch in patches:
            for line in patch:
                sys.stdout.write(line)

    return 0


def register_commands(subparsers):
    parser_updatesrcrev = subparsers.add_parser('updatesrcrev',
                                          help='Update SRCREV variable with the latest revision',
                                          description='Adds/updates the value a variable is set to in a recipe')
    parser_updatesrcrev.add_argument('recipefile', help='Recipe file to update')
    parser_updatesrcrev.add_argument('--patch', '-p', help='Create a patch to make the change instead of modifying the recipe', action='store_true')
    parser_updatesrcrev.set_defaults(func=updatesrcrev)
