package org.valkyrienskies.loader.fabric

import net.minecraft.util.math.ChunkPos
import org.valkyrienskies.core.game.ChunkClaim

fun ChunkClaim.contains(pos: ChunkPos) = contains(pos.x, pos.z)