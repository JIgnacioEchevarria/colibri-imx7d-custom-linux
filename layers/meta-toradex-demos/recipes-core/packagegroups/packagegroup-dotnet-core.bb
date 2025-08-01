DESCRIPTION = "Provides runtime dependencies for .NET Core 2.0"

# libcurl gets dynamically renamed
PACKAGE_ARCH = "${TUNE_PKGARCH}"
inherit packagegroup

PACKAGES = "packagegroup-dotnet-deps"

RDEPENDS:packagegroup-dotnet-deps = "\
    libunwind \
    icu \
    libcurl \
"
