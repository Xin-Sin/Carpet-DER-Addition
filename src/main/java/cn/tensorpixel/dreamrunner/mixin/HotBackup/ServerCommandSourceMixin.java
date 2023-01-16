package cn.tensorpixel.dreamrunner.mixin.HotBackup;

import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerCommandSource.class)
public interface ServerCommandSourceMixin {
    @Accessor
    CommandOutput getOutput();
}
