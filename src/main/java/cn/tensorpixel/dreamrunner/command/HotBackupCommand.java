package cn.tensorpixel.dreamrunner.command;

import cn.tensorpixel.dreamrunner.CarpetDERAdditionSettings;
import cn.tensorpixel.dreamrunner.mixin.HotBackup.MinecraftServerMixin;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import lombok.SneakyThrows;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.command.argument.TextArgumentType.text;
import static net.minecraft.command.argument.TextArgumentType.getTextArgument;
public class HotBackupCommand {
    public static boolean isReplace = false;
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        LiteralArgumentBuilder<ServerCommandSource> command = literal("bk")
                .requires(serverCommandSource -> CarpetDERAdditionSettings.HotBackup)
                .executes(HotBackupCommand::backupWithoutCommit)
                .then(
                        argument("commit", text())
                                .executes(HotBackupCommand::backup)
                );

        dispatcher.register(command);
    }
    @SneakyThrows
    private static void backup(ServerCommandSource source, String commit){
        MinecraftServer server = source.getServer();
        PlayerManager playerManager = server.getPlayerManager();
        List<ServerPlayerEntity> playerList = playerManager.getPlayerList();
//        server.
        MinecraftServerMixin mixin = (MinecraftServerMixin) server;
        WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory = mixin.getWorldGenerationProgressListenerFactory();
        WorldGenerationProgressListener worldGenerationProgressListener = worldGenerationProgressListenerFactory.create(11);
        Method createWorlds = MinecraftServer.class.getDeclaredMethod("createWorlds", WorldGenerationProgressListener.class);
        createWorlds.invoke(server, worldGenerationProgressListener);
        isReplace = !isReplace;
    }
    private static int backup(CommandContext<ServerCommandSource> cmc){
        Text commit = getTextArgument(cmc, "commit");
        backup(cmc.getSource(),commit.asString());
        return 0;
    }
    private static int backupWithoutCommit(CommandContext<ServerCommandSource> cmc){
        backup(cmc.getSource(),"");
        return 0;
    }
}
