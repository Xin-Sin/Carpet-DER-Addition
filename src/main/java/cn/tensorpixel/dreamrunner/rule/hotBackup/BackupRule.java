package cn.tensorpixel.dreamrunner.rule.hotBackup;

import cn.tensorpixel.dreamrunner.entity.BackupData;
import cn.tensorpixel.dreamrunner.util.FileUtilByBackup;
import cn.tensorpixel.dreamrunner.util.MessageUtil;
import cn.tensorpixel.dreamrunner.util.StringConstant;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import java.io.File;

public class BackupRule implements Runnable {
    private BackupData backupData;
    private ServerCommandSource source;
    private long oldTime = 0L;

    public BackupRule(BackupData backupData, ServerCommandSource source, long oldTime) {
        this.backupData = backupData;
        this.source = source;
        this.oldTime = oldTime;
    }

    public BackupRule() {
    }

    private void back(BackupData backupData){
        FileUtilByBackup.copyFolder(new File(StringConstant.WORLD),new File(StringConstant.BACKUP),backupData);
        Save.saveOn(source);
        long newTime = System.currentTimeMillis();
        source.sendFeedback(MessageUtil.getText("备份成功-注释:"
                + (backupData.getCommit().equals("") ? "空" : backupData.getCommit())
                + ", 耗时: " + ((newTime - oldTime) / 1000)
                + "秒",Formatting.GREEN), false);
    }
    public int list(ServerCommandSource source) throws CommandSyntaxException {
        StringBuilder list = FileUtilByBackup.list(new File("backup"));
        String[] split = list.toString().split("\n");
        for (String s: split){
            source.getPlayer().sendMessage(MessageUtil.getText(s,Formatting.LIGHT_PURPLE),false);
        }
        return 1;
    }
    public int delete(int num){

        return 1;
    }
    @Override
    public void run() {
        back(this.backupData);
    }
}
