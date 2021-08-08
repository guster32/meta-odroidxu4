require mali.inc

DESCRIPTION = "Mali t62x GPU Binaries for ODROID-xu3/4"
LIC_FILES_CHKSUM = "file://END_USER_LICENCE_AGREEMENT.txt;md5=3918cc9836ad038c5a090a0280233eea"

TYPE = "mali-t62x"

DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'virtual/mesa', '', d)}"

BRANCH = "mali-t62x_r12p0_04rel0"
SRCREV = "abf9740808ef0260e01f2277fc66c656393025e5"
SRC_URI = "git://github.com/guster32/arm-mali.git;branch=${BRANCH}"

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
        install ${S}/${TYPE}/wayland/liboffline_compiler_api.so ${D}/${libdir}
        install ${S}/${TYPE}/wayland/libump.so ${D}/${libdir}
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
    ln -sf libmali.so ${D}/${libdir}/libGLESv2.so.2
    ln -sf libGLESv2.so.2 ${D}/${libdir}/libGLESv2.so
    ln -sf libmali.so ${D}/${libdir}/libOpenCL.so.1
    ln -sf libOpenCL.so.1 ${D}/${libdir}/libOpenCL.so

	if [ "${USE_WL}" = "yes" ]; then
		ln -sf libmali.so ${D}/${libdir}/libgbm.so.1
		ln -sf libgbm.so.1 ${D}/${libdir}/libgbm.so
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

    install -d ${D}${libdir}
    install -d ${D}${includedir}
}

# Inhibit warnings about files being stripped
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"

RREPLACES_${PN} = "libegl libgles1 libglesv1-cm1 libgles2 libglesv2-2 libgbm"
RPROVIDES_${PN} = "libegl libgles1 libglesv1-cm1 libgles2 libglesv2-2 libgbm"
RCONFLICTS_${PN} = "libegl libgles1 libglesv1-cm1 libgles2 libglesv2-2 libgbm"

FILES_${PN} = "${libdir}/*"

#RDEPENDS_${PN} += "kernel-module-mali-t62x"
 
COMPATIBLE_MACHINE = "odroid-xu4"

