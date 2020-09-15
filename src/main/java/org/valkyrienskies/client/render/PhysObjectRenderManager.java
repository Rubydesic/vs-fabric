package org.valkyrienskies.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import org.joml.AxisAngle4d;
import org.joml.Matrix4d;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;
import org.valkyrienskies.loader.fabric.PhysicsObject;
import org.valkyrienskies.loader.fabric.client.render.GarbageBlockModelRenderer;
import org.valkyrienskies.loader.fabric.client.render.PhysRenderChunk;

import java.util.HashMap;
import java.util.Map;

/**
 * Object owned by each physObject responsible for handling all rendering operations
 *
 * @author thebest108
 */
public class PhysObjectRenderManager {

    // This pos is used to prevent Z-Buffer Errors D:
    // It's actual value is completely irrelevant as long as it's close to the
    // Ship's centerBlockPos
    public final BlockPos offsetPos;
    private final PhysicsObject parent;
    private final Map<ChunkPos, PhysRenderChunk> renderChunks;

    public PhysObjectRenderManager(PhysicsObject toRender, BlockPos offsetPos) {
        this.parent = toRender;
        this.offsetPos = offsetPos;
        this.renderChunks = new HashMap<>();
        parent.getChunks()
            .iterateChunks((x, z) -> {
                Chunk chunk = parent.getWorld().getChunk(x, z);
                renderChunks.put(chunk.getPos(), new PhysRenderChunk(parent, chunk));
                return null;
            });
    }

    public void renderBlockLayer(RenderLayer layerToRender, double partialTicks, MatrixStack stack) {
        GL11.glPushMatrix();

//        applyRenderTransform(partialTicks, false);
        layerToRender.startDrawing();
        for (PhysRenderChunk renderChunk : renderChunks.values()) {
            renderChunk.renderBlockLayer(layerToRender, stack);
        }
        layerToRender.endDrawing();

        GL11.glPopMatrix();
    }

    public void killRenderers() {
//        if (renderChunks != null) {
//            for (OldPhysRenderChunk renderChunk : renderChunks.values()) {
//                renderChunk.killRenderChunk();
//            }
//        }
    }

//    public void updateRange(int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
//        boolean updateImmediately) {
//        if (renderChunks == null || parent == null || parent.getChunkClaim() == null) {
//            return;
//        }
//
//        int size = (maxX + 1 - minX) * (maxZ + 1 - minZ) * (maxY + 1 - minY);
//
//        if (size > 65535) {
//            return;
//        }
//
//        int minChunkX = minX >> 4;
//        int maxChunkX = maxX >> 4;
//        int minChunkZ = minZ >> 4;
//        int maxChunkZ = maxZ >> 4;
//
//        int minBlockArrayY = Math.max(0, minY >> 4);
//        int maxBlockArrayY = Math.min(15, maxY >> 4);
//
//        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
//            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
//                ChunkPos pos = new ChunkPos(chunkX, chunkZ);
//                if (renderChunks.containsKey(pos)) {
//                    renderChunks.get(pos).updateLayers(minBlockArrayY, maxBlockArrayY);
//                }
//            }
//        }
//    }
//
//    public boolean shouldRender(ICamera camera) {
//        return camera == null || camera.isBoundingBoxInFrustum(parent.getShipBoundingBox());
//    }

    public void applyRenderTransform(double partialTicks, boolean inverse) {
        Matrix4d renderTransform = parent.getRenderTransform();
        Vector3d translation = renderTransform.getTranslation(new Vector3d());
        GL11.glTranslated(translation.x, translation.y, translation.z);

        AxisAngle4d rotation = new AxisAngle4d().set(renderTransform);
        GL11.glRotated(rotation.angle, rotation.x, rotation.y, rotation.z);

//        Vector3d centerOfRotation = new Vector3d(parent.getChunks().getX() * 16, 60, parent.getChunks().getZ() * 16);
//
//        Entity player = MinecraftClient.getInstance().player;
//
//        double p0 = player.lastRenderX
//            + (player.getPos().x - player.lastRenderX)
//            * partialTicks;
//        double p1 = player.lastRenderY
//            + (player.getPos().y -player.lastRenderY)
//            * partialTicks;
//        double p2 = player.lastRenderZ
//            + (player.getPos().z - player.lastRenderZ)
//            * partialTicks;
//
//        Matrix4d renderTransform = parent.getTransform();
//        Vector3d renderPos = renderTransform.getTranslation(new Vector3d());
//
//        // Offset pos is used to prevent floating point errors when rendering stuff thats very far away.
//        double offsetX = offsetPos.getX() - centerOfRotation.x();
//        double offsetY = offsetPos.getY() - centerOfRotation.y();
//        double offsetZ = offsetPos.getZ() - centerOfRotation.z();
//
//        if (inverse) {
//            AxisAngle4d rotation = new AxisAngle4d().set(parent.getGlobalToSubspace());
//
//            GL11.glTranslated(-offsetX, -offsetY, -offsetZ);
//            GL11.glRotated(Math.toDegrees(rotation.angle), rotation.x, rotation.y, rotation.z);
//            GL11.glTranslated(p0 - renderPos.x, p1 - renderPos.y, p2 - renderPos.z);
//        } else {
//            AxisAngle4d rotation = new AxisAngle4d().set(parent.getSubspaceToGlobal());
//
//            GL11.glTranslated(-p0 + renderPos.x, -p1 + renderPos.y, -p2 + renderPos.z);
//            GL11.glRotated(Math.toDegrees(rotation.angle), rotation.x, rotation.y, rotation.z);
//            GL11.glTranslated(offsetX, offsetY, offsetZ);
//        }
    }

//    /**
//     * @see #renderDebugInfo(double, double, double)
//     */
//    public void renderDebugInfo(Vector3dc offset) {
//        this.renderDebugInfo(offset.x(), offset.y(), offset.z());
//    }
//
//    /**
//     * Renders bounding boxes for this ship's AABB and center of mass relative to the offset
//     * (probably the negative player's position). Used in F3+D
//     */
//    public void renderDebugInfo(double offsetX, double offsetY, double offsetZ) {
//        GlStateManager.pushMatrix();
//
//        AxisAlignedBB shipBB = parent.getShipBB().offset(offsetX, offsetY, offsetZ);
//        ShipTransform renderTransform = parent.getShipTransformationManager().getRenderTransform();
//
//        Vector3dc centerOfMass = parent.getShipData().getInertiaData().getGameTickCenterOfMass();
//
//        Vector3d centerOfMassPos = new Vector3d(centerOfMass);
//        parent.getShipTransformationManager().getRenderTransform().transformPosition(centerOfMassPos, TransformType.SUBSPACE_TO_GLOBAL);
//
//        AxisAlignedBB centerOfMassBB = new AxisAlignedBB(JOML.toMinecraft(centerOfMassPos), JOML.toMinecraft(centerOfMassPos))
//            .grow(.1).offset(offsetX, offsetY, offsetZ);
//
//        GlStateManager.depthMask(false);
//        GlStateManager.disableTexture2D();
//        GlStateManager.disableLighting();
//        GlStateManager.disableCull();
//        GlStateManager.disableBlend();
//
//        // Draw the bounding box for the ship.
//        RenderGlobal.drawSelectionBoundingBox(shipBB, 1.0F, 1.0F, 1.0F, 1.0F);
//
//
//        // region Render claimed chunks
//        if (VSConfig.renderShipChunkClaimsInDebug) {
//            GlStateManager.pushMatrix();
//
//            GlStateManager.translate((float) renderTransform.getPosX() + offsetX, (float) renderTransform.getPosY() + offsetY, (float) renderTransform.getPosZ() + offsetZ);
//
//            Vector3dc angles = renderTransform.getSubspaceToGlobal().getEulerAnglesZYX(new Vector3d());
//
//            GlStateManager.rotate((float) Math.toDegrees(angles.z()), 0, 0, 1);
//            GlStateManager.rotate((float) Math.toDegrees(angles.y()), 0, 1, 0);
//            GlStateManager.rotate((float) Math.toDegrees(angles.x()), 1, 0, 0);
//
//            for (ChunkPos claimedChunk : parent.getChunkClaim()) {
//                AxisAlignedBB claimBB = new AxisAlignedBB(claimedChunk.getXStart() + .1, renderTransform.getCenterCoord().y() - 8 + 0.1, claimedChunk.getZStart() + .1, claimedChunk.getXEnd() + .9, renderTransform.getCenterCoord().y() + 8 - .1, claimedChunk.getZEnd() + .9);
//                claimBB = claimBB.offset(-renderTransform.getCenterCoord().x(), -renderTransform.getCenterCoord().y(), -renderTransform.getCenterCoord().z());
//                RenderGlobal.drawSelectionBoundingBox(claimBB, 0, 1.0F, 0, 1.0F);
//            }
//            GlStateManager.popMatrix();
//        }
//        // endregion
//
//        // Draw the center of mass bounding box.
//        GlStateManager.disableDepth();
//        RenderGlobal.drawSelectionBoundingBox(centerOfMassBB, 0, 0, 1.0F, 1.0F);
//        GlStateManager.enableDepth();
//
//        GlStateManager.enableTexture2D();
//        GlStateManager.enableLighting();
//        GlStateManager.enableCull();
//        GlStateManager.disableBlend();
//        GlStateManager.depthMask(true);
//
//        // Draw a text box that shows the numerical value of the center of mass.
//        String centerOfMassStr = "Center of Mass: " + (new Vector3d(centerOfMass)).toString(new DecimalFormat("############.##"));
//        renderTextBox(centerOfMassStr, renderTransform.getPosX(), renderTransform.getPosY() + .5,
//            renderTransform.getPosZ(), offsetX, offsetY, offsetZ);
//
//        String massStr = String.format("Mass: %.2f", parent.getShipData().getInertiaData().getGameTickMass());
//
//        renderTextBox(massStr, renderTransform.getPosX(), renderTransform.getPosY() + 1,
//            renderTransform.getPosZ(), offsetX, offsetY, offsetZ);
//
//        GlStateManager.popMatrix();
//    }
//
//    /**
//     * Renders a text box in the world at the position xyz, with the render offset provided. (Render offset not included
//     * in text box distance check).
//     */
//    private void renderTextBox(String str, double posX, double posY, double posZ, double offsetX, double offsetY, double offsetZ) {
//        Minecraft mc = Minecraft.getMinecraft();
//        Entity renderViewEntity = mc.getRenderViewEntity();
//        if (renderViewEntity == null) {
//            return; // Skip rendering
//        }
//        final double maxDistance = 64;
//        double d0 = new Vec3d(posX, posY, posZ)
//            .squareDistanceTo(renderViewEntity.getPositionVector());
//
//        if (d0 <= maxDistance * maxDistance) {
//            float playerYaw = mc.getRenderManager().playerViewY;
//            float playerPitch = mc.getRenderManager().playerViewX;
//            boolean isThirdPersonFrontal = mc.getRenderManager().options.thirdPersonView == 2;
//            EntityRenderer.drawNameplate(mc.getRenderManager().getFontRenderer(), str,
//                (float) (posX + offsetX), (float) (posY + offsetY), (float) (posZ + offsetZ),
//                0, playerYaw, playerPitch, isThirdPersonFrontal, false);
//        }
//    }
//
//    /**
//     * Called when a chunk is being replaced with a new one. We replace the old chunk renderer with a new one as well.
//     */
//    public void updateChunk(@Nonnull Chunk chunk) {
//        ChunkPos chunkPos = new ChunkPos(chunk.x, chunk.z);
//        PhysRenderChunk oldRenderer = renderChunks.get(chunkPos);
//        if (oldRenderer != null) {
//            oldRenderer.killRenderChunk();
//        }
//        renderChunks.put(chunkPos, new PhysRenderChunk(parent, chunk));
//    }
}
