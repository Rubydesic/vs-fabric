package org.valkyrienskies.loader.fabric.client

import net.minecraft.client.render.BuiltChunkStorage
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.chunk.ChunkBuilder
import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.valkyrienskies.core.game.ChunkClaim
import java.util.*

class VSBuiltChunkStorage(
    chunkBuilder: ChunkBuilder,
    world: World,
    viewDistance: Int,
    worldRenderer: WorldRenderer
) : BuiltChunkStorage(chunkBuilder, world, 0, worldRenderer) {

    private val worldChunks = BuiltChunkStorage(chunkBuilder, world, viewDistance, worldRenderer)

    /**
     * Maps ChunkClaim to the chunkStorage for it
     */
    private val shipChunks = HashMap<ChunkClaim, BuiltChunkStorage>()

    override fun createChunks(chunkBuilder: ChunkBuilder) {
        // do nothing
    }

    private fun getRelevantChunkStorage(x: Int, y: Int, z: Int): BuiltChunkStorage {
        return worldChunks
    }

    override fun updateCameraPosition(camX: Double, camZ: Double) {
        worldChunks.updateCameraPosition(camX, camZ)
    }

    override fun clear() {
        worldChunks.clear()
    }

    override fun scheduleRebuild(x: Int, y: Int, z: Int, important: Boolean) {
        getRelevantChunkStorage(x, y, z).scheduleRebuild(x, y, z, important);
    }

    override fun getRenderedChunk(pos: BlockPos): BuiltChunk? {
        return getRelevantChunkStorage(pos.x, pos.y, pos.z).getRenderedChunk(pos)
    }
}