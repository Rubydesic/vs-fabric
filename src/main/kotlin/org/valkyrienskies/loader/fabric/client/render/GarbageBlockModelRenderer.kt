package org.valkyrienskies.loader.fabric.client.render

import net.minecraft.block.BlockState
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.Tessellator
import net.minecraft.util.math.BlockPos

object GarbageBlockModelRenderer : BlockModelRenderer {
    override fun renderBlockModel(
        tesselator: Tessellator,
        blockState: BlockState,
        pos: BlockPos,
        brightness: Int
    ) {
        if (blockState.isAir) return

        drawBoundingBox(tesselator.buffer,
            pos.x - 0.5, pos.y - 0.5, pos.z - 0.5,
            pos.x + 0.5, pos.y + 0.5, pos.z + 0.6,
            1.0f, 1.0f, 1.0f, 1.0f
        )
    }

    public fun drawBoundingBox(
        buffer: BufferBuilder,
        minX: Double,
        minY: Double,
        minZ: Double,
        maxX: Double,
        maxY: Double,
        maxZ: Double,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        buffer.vertex(minX, minY, minZ).color(red, green, blue, 0.0f).next()
        buffer.vertex(minX, minY, minZ).color(red, green, blue, alpha).next()
        buffer.vertex(maxX, minY, minZ).color(red, green, blue, alpha).next()
        buffer.vertex(maxX, minY, maxZ).color(red, green, blue, alpha).next()
        buffer.vertex(minX, minY, maxZ).color(red, green, blue, alpha).next()
        buffer.vertex(minX, minY, minZ).color(red, green, blue, alpha).next()
        buffer.vertex(minX, maxY, minZ).color(red, green, blue, alpha).next()
        buffer.vertex(maxX, maxY, minZ).color(red, green, blue, alpha).next()
        buffer.vertex(maxX, maxY, maxZ).color(red, green, blue, alpha).next()
        buffer.vertex(minX, maxY, maxZ).color(red, green, blue, alpha).next()
        buffer.vertex(minX, maxY, minZ).color(red, green, blue, alpha).next()
        buffer.vertex(minX, maxY, maxZ).color(red, green, blue, 0.0f).next()
        buffer.vertex(minX, minY, maxZ).color(red, green, blue, alpha).next()
        buffer.vertex(maxX, maxY, maxZ).color(red, green, blue, 0.0f).next()
        buffer.vertex(maxX, minY, maxZ).color(red, green, blue, alpha).next()
        buffer.vertex(maxX, maxY, minZ).color(red, green, blue, 0.0f).next()
        buffer.vertex(maxX, minY, minZ).color(red, green, blue, alpha).next()
        buffer.vertex(maxX, minY, minZ).color(red, green, blue, 0.0f).next()
    }
}