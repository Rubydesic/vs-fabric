package org.valkyrienskies.loader.fabric.mixin;

import net.minecraft.client.render.chunk.ChunkBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ChunkBuilder.BuiltChunk.class)
public class MixinBuiltChunk {


    @Overwrite
    public boolean shouldBuild() {
        return true;
    }

//    @Shadow
//    public Box boundingBox;
//    @Shadow @Final private BlockPos.Mutable origin;
//
//
//    // Reference to parent
//    @SuppressWarnings("ShadowTarget")
//    @Shadow private ChunkBuilder field_20833;
//
//    /**
//     * @author rubydesic
//     */
//    @Overwrite
//    protected double getSquaredCameraDistance() {
//        World world = ((ChunkBuilderAccess) field_20833).getWorld();
//        TransformableChunks chunks = ValkyrienUtilsKt.getTransformablechunks(world,
//            (int) ((this.boundingBox.minX + 8) / 16),
//            (int) ((this.boundingBox.minZ + 8) / 16));
//
//        if (chunks == null) {
//            // Vanilla code
//            Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
//            double d = this.boundingBox.minX + 8.0D - camera.getPos().x;
//            double e = this.boundingBox.minY + 8.0D - camera.getPos().y;
//            double f = this.boundingBox.minZ + 8.0D - camera.getPos().z;
//            return d * d + e * e + f * f;
//        } else {
//            double d = this.
//        }
//
//
//    }

}
