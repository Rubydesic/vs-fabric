package org.valkyrienskies.loader.fabric.mixin;

import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderLayer.class)
public class MixinRenderLayer {

    @Final
    @Shadow
    public static RenderLayer SOLID;

    @Final
    @Shadow
    public static RenderLayer CUTOUT_MIPPED;

    @Final
    @Shadow
    public static RenderLayer CUTOUT;

    @Final
    @Shadow
    public static RenderLayer TRANSLUCENT;

}
