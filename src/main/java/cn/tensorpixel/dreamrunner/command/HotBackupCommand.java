package cn.tensorpixel.dreamrunner.command;

import cn.tensorpixel.dreamrunner.CarpetDERAdditionSettings;
import cn.tensorpixel.dreamrunner.entity.BackupData;
import cn.tensorpixel.dreamrunner.mixin.HotBackup.ServerCommandSourceMixin;
import cn.tensorpixel.dreamrunner.rule.hotBackup.BackupRule;
import cn.tensorpixel.dreamrunner.rule.hotBackup.Save;
import cn.tensorpixel.dreamrunner.util.StringConstant;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.util.Date;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
public class HotBackupCommand {
    public static boolean isReplace = false;
    private static BackupRule backupRule = new BackupRule();
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        LiteralArgumentBuilder<ServerCommandSource> command = literal("bk")
                .requires(serverCommandSource -> CarpetDERAdditionSettings.HotBackup)
                .then(
                        literal("create")
                                .executes(context -> backup(context.getSource(), ""))
                                .then(argument("commit", StringArgumentType.greedyString())
                                        .executes(context -> backup(context.getSource(),context.getArgument("commit",String.class))))
                )
                .then(
                        literal("delete")
                                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(4))
                                .then(
                                        argument("id", IntegerArgumentType.integer(1,5))
                                                .executes(HotBackupCommand::delete)
                                )
                )
                .then(
                        literal("back")
                                .then(
                                        argument("id", IntegerArgumentType.integer(1,5))
                                                .executes(HotBackupCommand::back)
                                )
                                .then(
                                        literal("confirm")
                                                .executes(HotBackupCommand::confirm)
                                )
                                .then(
                                        literal("cancel")
                                                .executes(HotBackupCommand::cancel)
                                )
                )
                .then(
                        literal("list")
                                .executes(HotBackupCommand::list)
                );
        dispatcher.register(command);
    }

    private static int list(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) {
        try {
            return backupRule.list(serverCommandSourceCommandContext.getSource());
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static int cancel(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) {
        return 0;
    }

    private static int confirm(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) {
        return 0;
    }

    private static int back(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) {
        return 0;
    }
    private static int delete(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) {
        return 0;
    }
    private static int backup(ServerCommandSource scs,String commit){
        int i = Save.saveOff(scs);
//        MinecraftServer server = scs.getServer();
//        PlayerManager playerManager = server.getPlayerManager();
//        List<ServerPlayerEntity> playerList = playerManager.getPlayerList();
//        for (ServerPlayerEntity serverPlayerEntity : playerList) {
//            if (serverPlayerEntity instanceof EntityPlayerMPFake entityPlayerMPFake){
//                entityPlayerMPFake.
//            }
//        }

        if (i != -1){
            long oldTime = System.currentTimeMillis();
            scs.getServer().saveAll(true, true, true);
//            创建备份注释
            BackupData backupData = new BackupData();
            backupData.setCommit(commit);
            backupData.setCreateTime(new Date());
            CommandOutput output = ((ServerCommandSourceMixin) scs).getOutput();
//            判断指令是否来自后台
            if (scs.getServer() != output){
                backupData.setCreator(scs.getName());
            }else{
                backupData.setCreator(StringConstant.CONSOLE_COMMAND_NAME);
            }
            new Thread(new BackupRule(backupData,scs,oldTime)).start();
            return 1;
        }else {
            return i;
        }
    }
}
