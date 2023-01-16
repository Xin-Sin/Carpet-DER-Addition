package cn.tensorpixel.dreamrunner.command;

import carpet.patches.EntityPlayerMPFake;
import cn.tensorpixel.dreamrunner.CarpetDERAdditionSettings;
import cn.tensorpixel.dreamrunner.entity.BackupData;
import cn.tensorpixel.dreamrunner.mixin.HotBackup.MinecraftServerMixin;
import cn.tensorpixel.dreamrunner.rule.hotBackup.Backup;
import cn.tensorpixel.dreamrunner.rule.hotBackup.Save;
import cn.tensorpixel.dreamrunner.util.StringConstant;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.SneakyThrows;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.command.argument.TextArgumentType.text;
import static net.minecraft.command.argument.TextArgumentType.getTextArgument;
public class HotBackupCommand {
    public static boolean isReplace = false;
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        LiteralArgumentBuilder<ServerCommandSource> command = literal("bk")
                .requires(serverCommandSource -> CarpetDERAdditionSettings.HotBackup)
                .then(literal("create").executes(context -> backup(context.getSource(), "")).then(argument("commit", StringArgumentType.greedyString()).executes(context -> backup(context.getSource(),context.getArgument("commit",String.class)))))
                .then(literal("delete"))
                .then(literal("back"))
                .then(literal("list"));
        dispatcher.register(command);
    }
/*    @SneakyThrows
    private static void backup(ServerCommandSource source, String commit){
        MinecraftServer server = source.getServer();
        PlayerManager playerManager = server.getPlayerManager();
        List<ServerPlayerEntity> playerList = playerManager.getPlayerList();
        MinecraftServerMixin mixin = (MinecraftServerMixin) server;
        WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory = mixin.getWorldGenerationProgressListenerFactory();
        WorldGenerationProgressListener worldGenerationProgressListener = worldGenerationProgressListenerFactory.create(11);
        Method createWorlds = MinecraftServer.class.getDeclaredMethod("createWorlds", WorldGenerationProgressListener.class);
        createWorlds.invoke(server, worldGenerationProgressListener);
        isReplace = !isReplace;
    }*/
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
            scs.getServer().saveAll(true, true, true);
//            创建备份注释
            BackupData backupData = new BackupData();
            try {
                backupData.setCommit(commit);
                backupData.setCreateTime(new Date());
                backupData.setCreator(scs.getPlayer().getUuid());
            } catch (CommandSyntaxException e) {
                backupData.setCreator(UUID.fromString(StringConstant.CONSOLE_COMMAND_UUID));
            }
            new Thread(new Backup(backupData)).start();
            int i1 = Save.saveOn(scs);
            if (i1 != -1){
                return 1;
            }else {
                return i1;
            }
        }else {
            return i;
        }
    }
}
