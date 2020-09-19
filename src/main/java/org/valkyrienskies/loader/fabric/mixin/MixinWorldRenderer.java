package org.valkyrienskies.loader.fabric.mixin;

import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.WorldRenderer.ChunkInfo;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.valkyrienskies.loader.fabric.ValkyrienUtilsKt;
import org.valkyrienskies.loader.fabric.VectorsMCKt;
import org.valkyrienskies.loader.fabric.client.VSBuiltChunkStorage;

@Mixin(WorldRenderer.class)
@Debug(export = true)
public class MixinWorldRenderer {

    private final WorldRenderer self = WorldRenderer.class.cast(this);

    @Shadow
    private ClientWorld world;

    @Shadow
    private BuiltChunkStorage chunks;

    @Inject(
        method = "renderLayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gl/VertexBuffer;bind()V"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void rotateRender(RenderLayer renderLayer, MatrixStack matrixStack, double d, double e, double f,
                              CallbackInfo ci, boolean bl, ObjectListIterator objectListIterator,
                              ChunkInfo chunkInfo2, ChunkBuilder.BuiltChunk builtChunk, VertexBuffer vertexBuffer) {
        int chunkX = builtChunk.getOrigin().getX() / 16;
        int chunkZ = builtChunk.getOrigin().getZ() / 16;
        ValkyrienUtilsKt.getShips(world)
            .stream()
            .filter(tchunks -> tchunks.getChunks().contains(chunkX, chunkZ))
            .findAny()
            // TODO: change to #getSubspaceToGlobal
            .ifPresent(tchunks -> VectorsMCKt.applyTransform(tchunks.getTransform().getOriginToGlobal(), matrixStack));
    }

    @Shadow @Final private ObjectList<ChunkInfo> visibleChunks;
    @Shadow private ChunkBuilder chunkBuilder;

    @Redirect(
        method = "reload",
        at = @At(
            value = "NEW",
            target = "Lnet/minecraft/client/render/BuiltChunkStorage;"
        )
    )
    public BuiltChunkStorage construct(ChunkBuilder chunkBuilder, World world, int viewDistance, WorldRenderer worldRenderer) {
        return new VSBuiltChunkStorage(chunkBuilder, world, viewDistance, worldRenderer);
    }

    /**
     * @author rubydesic
     */
    @Overwrite
    public String getChunksDebugString() {
        return "Valkyrien Skies: TODO: FIX THIS";
    }

    @Inject(
        method = "setupTerrain",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lcom/google/common/collect/Sets;newLinkedHashSet()Ljava/util/LinkedHashSet;"
        )
    )
    public void injectShipChunksIntoVisible(Camera camera, Frustum frustum, boolean hasForcedFrustum, int frame, boolean spectator, CallbackInfo ci) {
        // TODO inject only visible ship chunks..
        ValkyrienUtilsKt.getShips(world)
            .stream()
            .flatMap(physo -> physo.getChunks().mapChunks(ChunkPos::new).stream())
            .forEach(pos -> {
                ChunkBuilder.BuiltChunk builtChunk = this.chunks.getRenderedChunk(new BlockPos(pos.x * 16, 4, pos.z * 16));
                Direction[] DIRECTIONS = Direction.values();
                if (builtChunk != null) {
                    for (Direction dir : DIRECTIONS) {
                        ChunkInfo info = ChunkInfoAccess.create(self, builtChunk, dir, 0);
                        builtChunk.createRebuildTask();
                        visibleChunks.add(info);
                    }
                }
            });
    }

}
