package cn.tensorpixel.dreamrunner.mixin.HotBackup;

import cn.tensorpixel.dreamrunner.command.HotBackupCommand;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.KeepAliveS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow public abstract ClientConnection getConnection();

    /**
     * @author wzpMC
     * @reason stop package send
     */
    @Overwrite
    public void sendPacket(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> listener) {
        if (packet instanceof KeepAliveS2CPacket || !HotBackupCommand.isReplace){
            try {
                this.getConnection().send(packet, listener);
            } catch (Throwable var6) {
                CrashReport crashReport = CrashReport.create(var6, "Sending packet");
                CrashReportSection crashReportSection = crashReport.addElement("Packet being sent");
                crashReportSection.add("Packet class", () -> packet.getClass().getCanonicalName());
                throw new CrashException(crashReport);
            }
        }
    }

}
