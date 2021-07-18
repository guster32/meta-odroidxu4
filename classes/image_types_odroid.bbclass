inherit image_types

#
# Create an image that can by written onto a SD card using dd for use
# with Odroid BSP family
#
#  -------------------------------------
# |  Binary   | Block offset| part type |
# |   name    | SD   | eMMC |(eMMC only)|
#  -------------------------------------
# | Bl1       | 1    | 0    |  1 (boot) |
# | Bl2       | 31   | 30   |  1 (boot) |
# | U-boot    | 63   | 62   |  1 (boot) |
# | Tzsw      | 2111 | 2110 |  1 (boot) |
# | Uboot Env | 2625 | 2560 |  0 (user) |
#  -------------------------------------
#
do_rootfs[depends] += " virtual/bootloader:do_packagedata"

generic_odroid_xu_wic_cmd() {
    dd if=${DEPLOY_DIR_IMAGE}/bl1.bin.hardkernel of=$out${IMAGE_NAME_SUFFIX}.wic conv=notrunc bs=512 seek=1
    dd if=${DEPLOY_DIR_IMAGE}/bl2.bin.hardkernel of=$out${IMAGE_NAME_SUFFIX}.wic conv=notrunc bs=512 seek=31
    dd if=${DEPLOY_DIR_IMAGE}/u-boot-dtb.bin of=$out${IMAGE_NAME_SUFFIX}.wic conv=notrunc bs=512 seek=63
    dd if=${DEPLOY_DIR_IMAGE}/tzsw.bin.hardkernel of=$out${IMAGE_NAME_SUFFIX}.wic conv=notrunc bs=512 seek=2111
    dd if=/dev/zero of=$out${IMAGE_NAME_SUFFIX}.wic conv=notrunc count=32 bs=512 seek="2625"
}

IMAGE_CMD_wic_append_odroid-xu4() {
    generic_odroid_xu_wic_cmd
}

