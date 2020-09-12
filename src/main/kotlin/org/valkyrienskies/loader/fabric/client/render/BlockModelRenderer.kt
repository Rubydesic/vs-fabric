package org.valkyrienskies.loader.fabric.client.render

import net.minecraft.block.BlockState
import net.minecraft.client.render.Tessellator
import net.minecraft.util.math.BlockPos

interface BlockModelRenderer {
    fun renderBlockModel(tesselator: Tessellator, blockState: BlockState, pos: BlockPos, brightness: Int)
}