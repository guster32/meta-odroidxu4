require recipes-bsp/u-boot/u-boot.inc
DESCRIPTION = "Odroid XU4 boot loader supported by the hardkernel product"
SECTION = "bootloaders"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

PROVIDES += "virtual/bootloader u-boot"

LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

BRANCH = "odroidxu4-v2017.05"

UBOOT_INITIAL_ENV = ""
SRC_URI = "\
	git://github.com/hardkernel/u-boot.git;branch=${BRANCH} \
    https://dn.odroid.com/toolchains/gcc-linaro-arm-linux-gnueabihf-4.9-2014.09_linux.tar.xz \
    "
SRCREV = "42ac93dcfbbb8a08c2bdc02e19f96eb35a81891a"

SRC_URI[sha256sum] = "0cffac0caea0eb3c8bdddfa14be011ce366680f40aeddbefc7cf23cb6d4f1891"

PR = "${PV}+git${SRCPV}"

DEPENDS = " python3-native"
UBOOT_SUFFIX ?= "bin"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}/git"
B = "${S}"

inherit uboot-boot-scr

DEPENDS += " bc-native "
TOOLCHAIN_PREFIX = "arm-linux-gnueabihf-"
COMPILER = "${WORKDIR}/gcc-linaro-arm-linux-gnueabihf-4.9-2014.09_linux/bin/arm-linux-gnueabihf-gcc"
EXTRA_OEMAKE = ' CROSS_COMPILE="${TOOLCHAIN_PREFIX}" \
	CC="${COMPILER}" \
	ARCH=arm \
	CFLAGS="" \
	LDFLAGS="" \
	HOSTCC="${BUILD_CC}" CPUS=${@oe.utils.cpu_count()} V=1 \
'

LINAROTOOLCHAIN = "4.9-2014.09"

PATH_prepend = "${WORKDIR}/gcc-linaro-arm-linux-gnueabihf-4.9-2014.09_linux/bin:"
 
do_configure () {
	oe_runmake mrproper
	oe_runmake ${UBOOT_MACHINE}
}

do_configure_append() {
	if [ -e ${WORKDIR}/boot.ini ]; then
		cp ${WORKDIR}/boot.ini ${B}/
	fi
	if [ -e ${WORKDIR}/config.ini ]; then
		cp ${WORKDIR}/config.ini ${B}/
	fi
}

do_compile () {
	oe_runmake
}

do_compile_append () {
	cp ${B}/${UBOOT_BINARY} ${S}/sd_fuse/u-boot.bin
}


COMPATIBLE_MACHINE = "odroid-xu4"
