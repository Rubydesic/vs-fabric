package org.valkyrienskies.loader.fabric.mixin;

import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkBuilder.class)
public interface ChunkBuilderAccess {

    @Accessor
    World getWorld();

}
