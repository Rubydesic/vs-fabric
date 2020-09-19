package org.valkyrienskies.loader.fabric.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.RenderWorldLastCallback;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Inject(method = "renderWorld", at = @At("RETURN"))
    public void renderWorld(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo cb) {
        RenderWorldLastCallback.EVENT.invoker().onRenderWorld();
    }
}