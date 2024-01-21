DESCRIPTION = "HardKernel Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-6.1:"
KBRANCH = "odroidxu4-6.1.y"

# https://www.spinics.net/lists/stable/msg692738.html
SRC_URI = " \
	git://github.com/hardkernel/linux.git;protocol=https;branch=${KBRANCH} \
	"

EXTRA_OEMAKE:append = " KBUILD=${B}"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

SRCREV = "fb8ff6cc24f4ef519d89ae65956be3c2714b9b88"

PV = "${LINUX_VERSION}+git${SRCPV}"

KCONF_BSP_AUDIT_LEVEL = "0"

S = "${WORKDIR}/git"
B = "${S}"

KBUILD_DEFCONFIG = "odroidxu4_defconfig"
LINUX_VERSION ?= "6.1.67"

HOSTTOOLS += " dtc bc "

DEPENDS += " dtc-native bc-native lzop-native "
KERNEL_EXTRA_FEATURES = ""

COMPATIBLE_MACHINE = "odroid-xu4"
