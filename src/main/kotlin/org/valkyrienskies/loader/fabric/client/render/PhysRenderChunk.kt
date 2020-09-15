package org.valkyrienskies.loader.fabric.client.render

import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.VertexBuffer
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.WorldChunk
import org.valkyrienskies.loader.fabric.PhysicsObject
import java.util.*
import kotlin.collections.HashMap

class PhysRenderChunk(val parent: PhysicsObject, val chunk: Chunk) {

    private val renderChunks: Array<PartialChunkRendererVBO?> = arrayOfNulls(16)

    init {
        for (i in 0 until 16)
            if (chunk.sectionArray[i] != null)
                renderChunks[i] = PartialChunkRendererVBO(chunk, i * 16, i * 16 + 15)
    }

    fun renderBlockLayer(layer: RenderLayer, stack: MatrixStack) {
        for (chunk in renderChunks) {
            chunk?.renderBlockLayer(layer, stack)
        }
    }

    class PartialChunkRendererVBO(val chunk: Chunk, val yMin: Int, val yMax: Int) {

        private val world = (chunk as WorldChunk).world;
        private val builder = BufferBuilder(10_000)


        private val layerDataMap = HashMap<RenderLayer, LayerData>()
        private class LayerData(layer: RenderLayer, var needsUpdate: Boolean = true) {
            val buffer: VertexBuffer = VertexBuffer(layer.vertexFormat)
        }

        private fun getLayerData(layer: RenderLayer) = layerDataMap.computeIfAbsent(layer) { LayerData(layer) }

        fun markDirty() {
            // todo: update tile entities?
            layerDataMap.values.forEach { it.needsUpdate = true }
        }

        fun renderBlockLayer(layer: RenderLayer, stack: MatrixStack) {
            val layerData = getLayerData(layer)
            val random = Math.random()
            if (layerData.needsUpdate || random < 0.0001) {
                updateBuffer(layer, stack)
            }
            renderVertexBuffer(layerData.buffer)
        }

        private fun updateBuffer(layer: RenderLayer, stack: MatrixStack) {
            val layerData = getLayerData(layer)
            val buffer = layerData.buffer

            builder.begin(layer.drawMode, layer.vertexFormat)

            stack.push()
            stack.translate(0.0, 50.0, 0.0) // translate up  so I can see what's being rendered

            for (x in chunk.pos.x * 16 until chunk.pos.x * 16 + 16) {
                for (z in chunk.pos.z * 16 until chunk.pos.z * 16 + 16) {
                    for (y in yMin..yMax) {
                        val pos = BlockPos(x, y, z)
                        val blockState: BlockState = chunk.getBlockState(pos)
                        stack.push()
                        stack.translate(x.toDouble(), y.toDouble(), z.toDouble())
                        if (RenderLayers.getBlockLayer(blockState) == layer) {
                            MinecraftClient.getInstance().blockRenderManager
                                .renderBlock(blockState, pos, world, stack, builder, false, Random())
                        }
                        stack.pop()
                    }
                }
            }

            stack.pop()

            builder.end()
            buffer.upload(builder)
            builder.reset()

            layerData.needsUpdate = false
        }

    }

}