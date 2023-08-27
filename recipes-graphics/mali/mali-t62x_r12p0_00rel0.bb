LICENSE = "Proprietary"
SECTION = "libs"

DEPENDS += " virtual/mesa patchelf-native "
DEPENDS += "${@bb.utils.contains("DISTRO_FEATURES", "x11", " virtual/libx11 libxext libdrm libxfixes libxdamage", " ", d)}"

PROVIDES = " virtual/gpu virtual/egl virtual/libgles1 virtual/libgles2 virtual/libgles3 "
PROVIDES += "${@bb.utils.contains("DISTRO_FEATURES", "wayland", " virtual/libgbm virtual/libwayland-egl ", " ", d)}"

INSANE_SKIP:${PN} = "ldflags dev-so"
INSANE_SKIP:${PN}:append:libc-musl = " file-rdeps"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"

USE_X11 = "${@bb.utils.contains("DISTRO_FEATURES", "x11", "yes", "no", d)}"
USE_DFB = "${@bb.utils.contains("DISTRO_FEATURES", "directfb", "yes", "no", d)}"
USE_WL = "${@bb.utils.contains("DISTRO_FEATURES", "wayland", "yes", "no", d)}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

# Shared libs from mali package build aren't versioned, so we need
# to force the .so files into the runtime package (and keep them
# out of -dev package).
FILES_SOLIBSDEV = ""

PACKAGE_ARCH = "${MACHINE_ARCH}"
PACKAGES = "${PN} ${PN}-dev"


DESCRIPTION = "Mali t62x GPU Binaries for ODROID-xu3/4"
LIC_FILES_CHKSUM = "file://END_USER_LICENCE_AGREEMENT.txt;md5=3918cc9836ad038c5a090a0280233eea"

TYPE = "mali-t62x"

BRANCH = "mali-t62x_r12p0_04rel0"
SRCREV = "532c057fe564e7b75479ac851f636ed31f035429"
SRC_URI = "git://github.com/guster32/arm-mali.git;protocol=https;branch=${BRANCH}"

S = "${WORKDIR}/git"

do_install () {
    # Create MALI manifest
    install -m 755 -d ${D}/${libdir}
    if [ "${USE_X11}" = "yes" ]; then
        install ${S}/${TYPE}/x11/libmali.so ${D}/${libdir}
        install ${S}/${TYPE}/x11/liboffline_compiler_api.so ${D}/${libdir}
        install ${S}/${TYPE}/x11/libump.so ${D}/${libdir}
	fi
    if [ "${USE_WL}" = "yes" ]; then
        install ${S}/${TYPE}/wayland/libmali.so ${D}/${libdir}
        install ${S}/${TYPE}/wayland/libmali.so ${D}/${libdir}/libgbm.so
        install ${S}/${TYPE}/wayland/liboffline_compiler_api.so ${D}/${libdir}
        install ${S}/${TYPE}/wayland/libump.so ${D}/${libdir}
        patchelf --set-soname libgbm.so ${D}/${libdir}/libgbm.so
    fi
    if [ "${USE_DFB}" = "yes" ]; then
        install ${S}/${TYPE}/fbdev/libmali.so ${D}/${libdir}
        install ${S}/${TYPE}/fbdev/liboffline_compiler_api.so ${D}/${libdir}
        install ${S}/${TYPE}/fbdev/libump.so ${D}/${libdir}
    fi

    ln -sf libmali.so ${D}/${libdir}/libEGL.so.1
    ln -sf libEGL.so.1 ${D}/${libdir}/libEGL.so
    ln -sf libmali.so ${D}/${libdir}/libGLESv1_CM.so.1
    ln -sf libGLESv1_CM.so.1 ${D}/${libdir}/libGLESv1_CM.so
    ln -sf libmali.so ${D}/${libdir}/libGLESv1.so.1
    ln -sf libGLESv1.so.1 ${D}/${libdir}/libGLESv1.so
    ln -sf libmali.so ${D}/${libdir}/libGLESv2.so.2
    ln -sf libGLESv2.so.2 ${D}/${libdir}/libGLESv2.so
    ln -sf libmali.so ${D}/${libdir}/libOpenCL.so.1
    ln -sf libOpenCL.so.1 ${D}/${libdir}/libOpenCL.so

	if [ "${USE_WL}" = "yes" ]; then
		ln -sf libgbm.so ${D}/${libdir}/libgbm.so.1
		ln -sf libmali.so ${D}/${libdir}/libwayland-egl.so.1
		ln -sf libwayland-egl.so.1 ${D}/${libdir}/libwayland-egl.so
	fi

        # install headers
    install -d -m 0655 ${D}${includedir}/EGL
    install -m 0644 ${S}/${TYPE}/headers/EGL/*.h ${D}${includedir}/EGL/
    install -d -m 0655 ${D}${includedir}/GLES
    install -m 0644 ${S}/${TYPE}/headers/GLES/*.h ${D}${includedir}/GLES/
    install -d -m 0655 ${D}${includedir}/GLES2
    install -m 0644 ${S}/${TYPE}/headers/GLES2/*.h ${D}${includedir}/GLES2/
    install -d -m 0655 ${D}${includedir}/KHR
    install -m 0644 ${S}/${TYPE}/headers/KHR/*.h ${D}${includedir}/KHR/
    install -d -m 0655 ${D}${includedir}/ump
    install -m 0644 ${S}/${TYPE}/headers/ump/*.h ${D}${includedir}/ump/
    install -d -m 0655 ${D}${includedir}/umplock
    install -m 0644 ${S}/${TYPE}/headers/umplock/*.h ${D}${includedir}/umplock/

    install -d ${D}${libdir}/pkgconfig
    install -m 0644 ${S}/${TYPE}/pkgconfig/egl.pc ${D}${libdir}/pkgconfig/egl.pc
    install -m 0644 ${S}/${TYPE}/pkgconfig/glesv2.pc ${D}${libdir}/pkgconfig/glesv2.pc
    install -m 0644 ${S}/${TYPE}/pkgconfig/glesv1.pc ${D}${libdir}/pkgconfig/glesv1.pc
    install -m 0644 ${S}/${TYPE}/pkgconfig/glesv1_cm.pc ${D}${libdir}/pkgconfig/glesv1_cm.pc
    install -m 0644 ${S}/${TYPE}/pkgconfig/ump.pc ${D}${libdir}/pkgconfig/ump.pc

    install -d ${D}${includedir}
}

# Inhibit warnings about files being stripped
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"

RREPLACES:${PN} += " libegl libgles1 libglesv1-cm1 libgles2 libglesv2-2 libOpenCL libGLESv2.so "
RPROVIDES:${PN} += " libegl libgles1 libglesv1-cm1 libgles2 libglesv2-2 libOpenCL libGLESv2.so "
RCONFLICTS:${PN} = " libegl libgles1 libglesv1-cm1 libgles2 libglesv2-2 libOpenCL libGLESv2.so "

RREPLACES:${PN} += "${@bb.utils.contains("DISTRO_FEATURES", "wayland", " libgbm libwayland-egl.so", " ", d)}"
RPROVIDES:${PN} += "${@bb.utils.contains("DISTRO_FEATURES", "wayland", " libgbm libwayland-egl.so", " ", d)}"
RCONFLICTS:${PN} += "${@bb.utils.contains("DISTRO_FEATURES", "wayland", " libgbm libwayland-egl.so", " ", d)}"
RDEPENDS:${PN} += "${@bb.utils.contains("DISTRO_FEATURES", "wayland", " udev ", " ", d)}"

FILES:${PN} += "${libdir}/lib*.so* "

#RDEPENDS:${PN} += "kernel-module-mali-t62x"

COMPATIBLE_MACHINE = "odroid-xu4"

