package org.valkyrienskies.loader.fabric.mixin;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@SuppressWarnings("InvokerTarget")
@Mixin(WorldRenderer.ChunkInfo.class)
public interface ChunkInfoAccess {

    @Invoker(value = "<init>")
    static WorldRenderer.ChunkInfo create(WorldRenderer renderer, ChunkBuilder.BuiltChunk chunk, @Nullable Direction direction, int propagationLevel) {
        throw new AssertionError();
    }
}