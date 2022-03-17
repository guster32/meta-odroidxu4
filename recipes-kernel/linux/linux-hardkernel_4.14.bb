DESCRIPTION = "HardKernel Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-4.14:"
KBRANCH = "odroidxu4-4.14.y"

SRC_URI = "git://github.com/hardkernel/linux.git;branch=${KBRANCH}"
SRC_URI += "file://0001-Fix-Mali-Gator-in-tree-build.patch"
SRC_URI += "file://0002-rtl8812au-do-not-Werror.patch"

SRCREV = "864c4519b77763274b61a035b33bc92f71084b59"
SRC_URI[sha256sum] = "0cffac0caea0eb3c8bdddfa14be011ce366680f40aeddbefc7cf23cb6d4f1891"

PV = "${LINUX_VERSION}+git${SRCPV}"

KCONF_BSP_AUDIT_LEVEL = "0"

S = "${WORKDIR}/git"
B = "${S}"

KBUILD_DEFCONFIG = "odroidxu4_defconfig"
LINUX_VERSION ?= "4.14.180"

HOSTTOOLS += " dtc bc "

DEPENDS += " dtc-native bc-native lzop-native "
KERNEL_EXTRA_FEATURES = ""

TOOLCHAIN_PREFIX = "arm-linux-gnueabihf-"

COMPILER = "${WORKDIR}/gcc-linaro-arm-linux-gnueabihf-4.9-2014.09_linux/bin/arm-linux-gnueabihf-gcc"

EXTRA_OECONF = ""
EXTRA_OEMAKE = ' CROSS_COMPILE="${TOOLCHAIN_PREFIX}" \
	CC="${COMPILER}" \
	ARCH=arm \
	CFLAGS="" \
	LDFLAGS="" \
	HOSTCC="${BUILD_CC}" CPUS=${@oe.utils.cpu_count()} V=1 \
    '

LINAROTOOLCHAIN = "4.9"

PATH:prepend = "${WORKDIR}/gcc-linaro-arm-linux-gnueabihf-4.9-2014.09_linux/bin:"

require recipes-kernel/linux/linux-yocto.inc

COMPATIBLE_MACHINE = "odroid-xu4"
