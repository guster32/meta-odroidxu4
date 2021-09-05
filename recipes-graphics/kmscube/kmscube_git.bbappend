#DEFINES += " MESA_EGL_NO_X11_HEADERS EGL_NO_X11 EGL_API_FB "
#PACKAGECONFIG += " -DMESA_EGL_NO_X11_HEADERS=enabled -DEGL_API_FB=enabled"
#EXTRA_OECMAKE_append = " -DMESA_EGL_NO_X11_HEADERS=enabled -DEGL_API_FB=enabled"
#EXTRA_OEMESON += " -DMESA_EGL_NO_X11_HEADERS=enabled -DEGL_API_FB=enabled "
CFLAGS += " -D__GBM__ "
DEPENDS += " ${@bb.utils.contains('MACHINE_FEATURES', 'mali', 'virtual/egl', '', d)}"
#RDEPENDS_${PN} += " libgbm"

SRCREV = "9f63f359fab1b5d8e862508e4e51c9dfe339ccb0"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://0001-Use-GLES2-instead-of-GLES3.patch \
            file://0002-Make-cube-shadertoy.c-work-with-GLES2.patch \
            file://0002-More.patch \
            file://0001-More-GLES2.patch \
            file://0002-Add-gbm-wrapper.patch \
            file://0003-No-gbm_bo_map.patch \
            file://0004-No-gbm_bo_unmap.patch \
            file://0005-Use-correct-gbm-modifier-for-exynos.patch \
            "

