package cn.tensorpixel.dreamrunner.util;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class MessageUtil {
    public static Text getText(String message, Formatting formatting){
        return Text.of(message).getWithStyle(Style.EMPTY.withColor(formatting)).get(0);
    }
}
