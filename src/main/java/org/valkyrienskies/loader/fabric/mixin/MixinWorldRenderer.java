package org.valkyrienskies.loader.fabric.mixin;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Quaternion;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.valkyrienskies.core.util.AttachedDataKt;
import org.valkyrienskies.core.util.VSCoreUtilKt;
import org.valkyrienskies.loader.fabric.PhysicsObject;
import org.valkyrienskies.loader.fabric.ShipData;
import org.valkyrienskies.loader.fabric.ValkyrienUtilsKt;

import java.util.Objects;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

//    @Inject(
//        method = "renderLayer",
//        at = @At(
//            value = "INVOKE",
//            target = "Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk;getOrigin()Lnet/minecraft/util/math/BlockPos;"),
//        locals = LocalCapture.CAPTURE_FAILHARD
//    )
//    private void rotateRender(RenderLayer renderLayer, MatrixStack matrixStack, double d, double e, double f,
//                              CallbackInfo ci, boolean bl, ObjectListIterator objectListIterator,
//                              WorldRenderer.ChunkInfo chunkInfo2, ChunkBuilder.BuiltChunk builtChunk, VertexBuffer vertexBuffer) {
//        if (builtChunk.getOrigin().getX() % 7 == 0) {
//            matrixStack.multiply(new Quaternion(45, 27, 30, true));
//        }
//    }

    @Shadow @Final private BufferBuilderStorage bufferBuilders;

    @Inject(
        method = "renderLayer",
        at = @At(value = "HEAD")
    )
    private void preRenderLayer(RenderLayer renderLayer, MatrixStack matrixStack, double d, double e, double f,
                                CallbackInfo ci) {
//        RenderHelper.disableStandardItemLighting();

        assert MinecraftClient.getInstance().world != null;
        ValkyrienUtilsKt.getShips(MinecraftClient.getInstance().world)
            .stream()
            .filter(Objects::nonNull)
            .forEach(physicsObject -> {
                GL11.glPushMatrix();
                if (physicsObject.getRenderer() == null) {
                    GL11.glPopMatrix();
                    return;
                } else {
                    physicsObject.getRenderer().renderBlockLayer(renderLayer, 1.0, matrixStack);
                }
                GL11.glPopMatrix();
            });

        // GlStateManager.resetColor();
    }

//    @Inject(method = "setupTerrain", at = @At("HEAD"))
//    private void setupTerrainPre(Camera camera, Frustum frustum,
//                              boolean hasForcedFrustum, int frame, boolean spectator, CallbackInfo cb) {
//        GL11.glPushMatrix();
//        GL11.glRotated(45, 1, 0, 0);
//    }
//
//    @Inject(method = "setupTerrain", at = @At("RETURN"))
//    private void setupTerrainPost(Camera camera, Frustum frustum,
//                              boolean hasForcedFrustum, int frame, boolean spectator, CallbackInfo cb) {
//        GL11.glPopMatrix();
//    }
}
