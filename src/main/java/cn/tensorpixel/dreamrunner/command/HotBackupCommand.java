package cn.tensorpixel.dreamrunner.command;

import cn.tensorpixel.dreamrunner.CarpetDERAdditionSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.command.argument.TextArgumentType.text;
public class HotBackupCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        LiteralArgumentBuilder<ServerCommandSource> command = literal("bk")
                .requires(serverCommandSource -> CarpetDERAdditionSettings.HotBackup)
                .executes(HotBackupCommand::backup)
                .then(argument("commit", text()))
                .executes(HotBackupCommand::backup);
        dispatcher.register(command);
    }
    private static int backup(CommandContext<ServerCommandSource> cmc){
        String commit = cmc.getArgument("commit", String.class);
        if (commit != null){

        } else {

        }
        return 0;
    }
}
