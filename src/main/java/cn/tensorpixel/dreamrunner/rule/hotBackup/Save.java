package cn.tensorpixel.dreamrunner.rule.hotBackup;

import cn.tensorpixel.dreamrunner.util.MessageUtil;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.concurrent.atomic.AtomicBoolean;

public class Save {
    public static int saveOff(ServerCommandSource source){
        AtomicBoolean bl = new AtomicBoolean(false);
        source.getServer().getWorlds().iterator().forEachRemaining(serverWorld ->{
            if (serverWorld != null && !serverWorld.savingDisabled){
                serverWorld.savingDisabled = true;
                bl.set(true);
            }
        });
        if (!bl.get()){
            source.sendError(Text.of("关闭世界自动保存时出现错误"));
            return -1;
        }else {
            source.sendFeedback(MessageUtil.getText("已关闭世界自动保存",Formatting.RED),true);
            return 1;
        }
    }
    public static int saveOn(ServerCommandSource source){
        AtomicBoolean bl = new AtomicBoolean(false);
        source.getServer().getWorlds().iterator().forEachRemaining(serverWorld ->{
            if (serverWorld != null && serverWorld.savingDisabled){
                serverWorld.savingDisabled = false;
                bl.set(true);
            }
        });
        if (!bl.get()){
            source.sendError(Text.of("开启自动世界保存时出现错误"));
            return -1;
        }else {
            source.sendFeedback(MessageUtil.getText("开启自动世界保存", Formatting.GREEN),true);
            return 1;
        }
    }
}
