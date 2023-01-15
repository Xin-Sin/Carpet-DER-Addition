package cn.tensorpixel.dreamrunner.mixin.HotBackup;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftServer.class)
public interface MinecraftServerMixin{
    @Accessor("worldGenerationProgressListenerFactory")
    WorldGenerationProgressListenerFactory getWorldGenerationProgressListenerFactory();
}
