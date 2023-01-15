package cn.tensorpixel.dreamrunner;

import carpet.settings.Rule;
import cn.tensorpixel.dreamrunner.util.Categories;

public class CarpetDERAdditionSettings {
    @Rule(desc = "backup/rollback with features",category = Categories.COMMAND)
    public static boolean HotBackup = false;
}
