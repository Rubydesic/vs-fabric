package org.valkyrienskies.loader.fabric

import com.googlecode.cqengine.attribute.Attribute
import com.googlecode.cqengine.attribute.MultiValueAttribute
import com.googlecode.cqengine.query.option.QueryOptions
import org.valkyrienskies.core.game.ChunkClaim
import org.valkyrienskies.core.physics.RigidBody
import org.valkyrienskies.core.physics.VoxelShape
import org.valkyrienskies.core.util.asLong
import org.valkyrienskies.loader.fabric.util.cqengine.attributek
import org.valkyrienskies.loader.fabric.util.cqengine.nullableAttributek
import java.util.*

class ShipData(
    val uuid: UUID,
    val rigidBody: RigidBody<VoxelShape>,
    val chunks: ChunkClaim,
    name: String?,
    var physicsEnabled: Boolean,
    @Transient var owner: QueryableShipData? = null,
    @Transient var physo: PhysicsObject? = null
) {

    var name = name
        set(value) {
            update(NAME)
            field = value
        }

    private fun update(attribute: Attribute<ShipData, *>) {
        owner?.getAllShips()?.updateObjectIndices(listOf(this), attribute)
    }

    companion object {
        val NAME: Attribute<ShipData, String> = nullableAttributek(ShipData::name)
        val UUID: Attribute<ShipData, UUID> = attributek(ShipData::uuid)

        val CHUNKS: Attribute<ShipData, Long> = object : MultiValueAttribute<ShipData, Long>() {
            override fun getValues(physo: ShipData, queryOptions: QueryOptions): Set<Long> {
                return physo.chunks.mapChunks(::asLong).toSet()
            }
        }
    }

}