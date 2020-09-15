package org.valkyrienskies.loader.fabric

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.joml.Matrix4d
import org.valkyrienskies.client.render.PhysObjectRenderManager
import org.valkyrienskies.core.game.ChunkClaim

class PhysicsObject(
    val world: World,
    val chunks: ChunkClaim,
    /**
     * <0,0,0> to global transform
     */
    val transform: Matrix4d
) {

    val subspaceToGlobal get() = transform
    val globalToSubspace get() = transform.invert(Matrix4d())
    val renderTransform get() = transform

    private var shouldRender = true

    val renderer = if (world.isClient) {
        PhysObjectRenderManager(this, /*BlockPos(chunks.x * 16, 0, chunks.z * 16)*/ BlockPos(0, 0, 0))
    } else { null }

}
