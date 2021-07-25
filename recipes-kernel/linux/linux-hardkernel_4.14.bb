DESCRIPTION = "HardKernel Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

KBRANCH = "odroidxu4-4.14.y"

SRC_URI = "git://github.com/hardkernel/linux.git;branch=${KBRANCH} \
    https://dn.odroid.com/toolchains/gcc-linaro-arm-linux-gnueabihf-4.9-2014.09_linux.tar.xz \
    "
#http://dn.odroid.com/ODROID-XU/compiler/arm-eabi-4.6.tar.gz
SRCREV = "864c4519b77763274b61a035b33bc92f71084b59"
SRC_URI[sha256sum] = "0cffac0caea0eb3c8bdddfa14be011ce366680f40aeddbefc7cf23cb6d4f1891"
#SRC_URI[sha256sum] = "4df101f7defe41f28551f78cf8a2da32c95fd85e6ad1ad4a8b7ef6564ca0b6f6"

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

PATH_prepend = "${WORKDIR}/gcc-linaro-arm-linux-gnueabihf-4.9-2014.09_linux/bin:"

do_install_prepend() {
    bbnote "custom kernel_do_install customization"
    cp ${B}/arch/arm/boot/dts/exynos5422-odroidxu3.dtb ${B}/arch/arm/boot
}

do_configure () {
    ln -s "${COMPILER}" "${WORKDIR}/gcc-linaro-arm-linux-gnueabihf-4.9-2014.09_linux/bin/arm-oe-linux-gnueabi-gcc" | true
    oe_runmake mrproper
	oe_runmake ${KBUILD_DEFCONFIG}
}

do_compile () {
    TARGET_SYS="arm-linux-gnueabihf" oe_runmake ${PARALLEL_MAKE}
}


COMPATIBLE_MACHINE = "odroid-xu4"
