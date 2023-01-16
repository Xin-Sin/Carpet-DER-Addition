package cn.tensorpixel.dreamrunner;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.settings.SettingsManager;
import cn.tensorpixel.dreamrunner.command.HotBackupCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CarpetDERAddition implements ModInitializer, CarpetExtension {

    public static final Logger log = LoggerFactory.getLogger("carpet-xinsin-addition");

    private static SettingsManager carpetDERAdditionSettingManager;
    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(CarpetDERAdditionSettings.class);
    }

    @Override
    public void onServerLoaded(MinecraftServer server) {

    }

    @Override
    public void onTick(MinecraftServer server) {

    }

    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        HotBackupCommand.register(dispatcher);
    }

    @Override
    public void onPlayerLoggedIn(ServerPlayerEntity player) {

    }

    @Override
    public void onServerClosed(MinecraftServer server) {

    }

    @Override
    public void onInitialize() {
        carpetDERAdditionSettingManager = new SettingsManager("1.0.1","carpetDERAddition","Carpet DER Addition");
        CarpetServer.manageExtension(new CarpetDERAddition());
    }
}
