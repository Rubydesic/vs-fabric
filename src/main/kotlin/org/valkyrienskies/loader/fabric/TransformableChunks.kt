package org.valkyrienskies.loader.fabric

import net.minecraft.world.World
import org.joml.Matrix4d
import org.joml.Matrix4dc
import org.valkyrienskies.core.game.ChunkClaim
import org.valkyrienskies.core.game.ChunksTransform

class TransformableChunks(
    val world: World,
    val chunks: ChunkClaim,
    transform: Matrix4dc
) {
    val transform = ChunksTransform(Matrix4d(transform), chunks)
}
