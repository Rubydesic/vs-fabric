package org.valkyrienskies.client.render;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.valkyrienskies.loader.fabric.PhysicsObject;
import org.valkyrienskies.loader.fabric.mixin.MixinRenderLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.valkyrienskies.loader.fabric.mixin.MixinRenderLayer.*;

public class PhysRenderChunk {

    public IVSRenderChunk[] renderChunks = new IVSRenderChunk[16];
    public PhysicsObject toRender;
    public Chunk chunk;

    public PhysRenderChunk(PhysicsObject toRender, Chunk chunk) {
        this.toRender = toRender;
        this.chunk = chunk;
        for (int i = 0; i < 16; i++) {
            ChunkSection storage = this.chunk.getSectionArray()[i];
            if (storage != null) {
                IVSRenderChunk renderChunk = new RenderLayerDisplayList(this.chunk, i * 16, i * 16 + 15, this);
                renderChunks[i] = renderChunk;
            }
        }
    }

    public void renderBlockLayer(RenderLayer layerToRender, MatrixStack stack, VertexConsumer consumer) {
        for (int i = 0; i < 16; i++) {
            IVSRenderChunk renderChunk = renderChunks[i];
            if (renderChunk != null) {
                renderChunk.renderBlockLayer(layerToRender, stack, consumer);
            }
        }
    }

    public void updateLayers(int minLayer, int maxLayer) {
        for (int layerY = minLayer; layerY <= maxLayer; layerY++) {
            IVSRenderChunk renderChunk = renderChunks[layerY];
            if (renderChunk != null) {
                renderChunk.markDirty();
            } else {
                IVSRenderChunk renderLayer = new RenderLayerDisplayList(this.chunk, layerY * 16, layerY * 16 + 15, this);
                renderChunks[layerY] = renderLayer;
            }
        }
    }

    void killRenderChunk() {
        for (int i = 0; i < 16; i++) {
            IVSRenderChunk renderChunk = renderChunks[i];
            if (renderChunk != null) {
                renderChunk.deleteRenderChunk();
            }
        }
    }

    private interface IVSRenderChunk {
        void renderBlockLayer(RenderLayer layerToRender, MatrixStack matrixStack, VertexConsumer consumer);

        void markDirty();

        void deleteRenderChunk();

        int minY();

        int maxY();
    }

    public static class RenderLayerDisplayList implements IVSRenderChunk {

        Chunk chunkToRender;
        int yMin, yMax;
        int glCallListCutout, glCallListCutoutMipped, glCallListSolid, glCallListTranslucent;
        PhysRenderChunk parent;
        boolean needsCutoutUpdate, needsCutoutMippedUpdate, needsSolidUpdate, needsTranslucentUpdate;
        List<BlockEntity> renderTiles = new ArrayList<>();

        RenderLayerDisplayList(Chunk chunk, int yMin, int yMax, PhysRenderChunk parent) {
            chunkToRender = chunk;
            this.yMin = yMin;
            this.yMax = yMax;
            this.parent = parent;
            markDirty();
            glCallListCutout = GL11.glGenLists(4);
            glCallListCutoutMipped = glCallListCutout + 1;
            glCallListSolid = glCallListCutout + 2;
            glCallListTranslucent = glCallListCutout + 3;
        }

        public int minY() {
            return yMin;
        }

        public int maxY() {
            return yMax;
        }

        public void markDirty() {
            needsCutoutUpdate = true;
            needsCutoutMippedUpdate = true;
            needsSolidUpdate = true;
            needsTranslucentUpdate = true;
            updateRenderTileEntities();
        }

        // TODO: There's probably a faster way of doing this.
        public void updateRenderTileEntities() {
//            ITileEntitiesToRenderProvider provider = (ITileEntitiesToRenderProvider) chunkToRender;
//            List<TileEntity> updatedRenderTiles = provider.getTileEntitiesToRender(yMin >> 4);
//            if (updatedRenderTiles != null) {
//                Minecraft.getMinecraft().renderGlobal
//                        .updateTileEntities(renderTiles, updatedRenderTiles);
//                renderTiles = new ArrayList<>(updatedRenderTiles);
//            }
        }

        public void deleteRenderChunk() {
            clearRenderLists();
//            Minecraft.getMinecraft().renderGlobal.updateTileEntities(renderTiles, new ArrayList<>());
            renderTiles.clear();
        }

        private void clearRenderLists() {
            GL11.glDeleteLists(glCallListCutout, 1);
            GL11.glDeleteLists(glCallListCutoutMipped, 1);
            GL11.glDeleteLists(glCallListSolid, 1);
            GL11.glDeleteLists(glCallListTranslucent, 1);
        }

        public void renderBlockLayer(RenderLayer layerToRender, MatrixStack matrixStack, VertexConsumer consumer) {
            if (layerToRender == CUTOUT) {
                if (needsCutoutUpdate) {
                    updateList(layerToRender, matrixStack, consumer);
                }
                GL11.glCallList(glCallListCutout);
            } else if (layerToRender == CUTOUT_MIPPED) {
                if (needsCutoutMippedUpdate) {
                    updateList(layerToRender, matrixStack, consumer);
                }
                GL11.glCallList(glCallListCutoutMipped);
            } else if (layerToRender == SOLID) {
                if (needsSolidUpdate) {
                    updateList(layerToRender, matrixStack, consumer);
                }
                GL11.glCallList(glCallListSolid);
            } else if (layerToRender == MixinRenderLayer.TRANSLUCENT) {
                if (needsTranslucentUpdate) {
                    updateList(layerToRender, matrixStack, consumer);
                }
                GL11.glCallList(glCallListTranslucent);
            }
        }

        private void updateList(RenderLayer layerToUpdate, MatrixStack matrixStack, VertexConsumer consumer) {
//            if (parent.toRender.getShipRenderer() == null) {
//                return;
//            }
//            BlockPos offsetPos = parent.toRender.getShipRenderer().offsetPos;
//            if (offsetPos == null) {
//                return;
//            }
            BlockPos offsetPos = new BlockPos(5, 5, 5);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder worldRenderer = tessellator.getBuffer();
            worldRenderer.begin(7, VertexFormats.POSITION_TEXTURE_LIGHT_COLOR);
            GL11.glPushMatrix();

            if (CUTOUT == layerToUpdate) {
                GL11.glDeleteLists(glCallListCutout, 1);
                GL11.glNewList(glCallListCutout, GL11.GL_COMPILE);
            } else if (CUTOUT_MIPPED == layerToUpdate) {
                GL11.glDeleteLists(glCallListCutoutMipped, 1);
                GL11.glNewList(glCallListCutoutMipped, GL11.GL_COMPILE);
            } else if (SOLID == layerToUpdate) {
                GL11.glDeleteLists(glCallListSolid, 1);
                GL11.glNewList(glCallListSolid, GL11.GL_COMPILE);
            } else if (TRANSLUCENT == layerToUpdate) {
                GL11.glDeleteLists(glCallListTranslucent, 1);
                GL11.glNewList(glCallListTranslucent, GL11.GL_COMPILE);
            }

            GL11.glPushMatrix();
            // worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            BlockState blockState;

            World world = ((WorldChunk) chunkToRender).getWorld();

            BlockPos.Mutable pos = new BlockPos.Mutable();
            for (int x = chunkToRender.getPos().x * 16; x < chunkToRender.getPos().x * 16 + 16; x++) {
                for (int z = chunkToRender.getPos().z * 16; z < chunkToRender.getPos().z * 16 + 16; z++) {
                    for (int y = yMin; y <= yMax; y++) {
                        pos.set(x + offsetPos.getX(), y + offsetPos.getY(), z + offsetPos.getZ());
                        blockState = chunkToRender.getBlockState(pos);
                        try {
                            if (true /* blockState.getBlock().canRenderInLayer(blockState, layerToUpdate) */) {
//                                MinecraftClient.getInstance().getBlockRenderManager()
//                                    .renderBlock(blockState, pos, world, matrixStack, consumer, false, ThreadLocalRandom.current());
                                MinecraftClient.getInstance().getBlockRenderManager()
                                    .renderBlockAsEntity(blockState,
                                        matrixStack,
                                        MinecraftClient.getInstance()
                                            .getBufferBuilders()
                                            .getEffectVertexConsumers(),
                                        15,
                                        15
                                        );
                            }
                        } catch (NullPointerException e) {
                            System.out.println(
                                "Something was null! LValkyrienSkiesBase/render/PhysRenderChunk#updateList");
                        }
                    }
                }
            }
            tessellator.draw();
            // worldrenderer.finishDrawing();
            GL11.glPopMatrix();
            GL11.glEndList();
            GL11.glPopMatrix();

            if (CUTOUT == layerToUpdate) {
                needsCutoutUpdate = false;
            } else if (CUTOUT_MIPPED == layerToUpdate) {
                needsCutoutMippedUpdate = false;
            } else if (SOLID == layerToUpdate) {
                needsSolidUpdate = false;
            } else if (TRANSLUCENT == layerToUpdate) {
                needsTranslucentUpdate = false;
            }
        }
    }
}
