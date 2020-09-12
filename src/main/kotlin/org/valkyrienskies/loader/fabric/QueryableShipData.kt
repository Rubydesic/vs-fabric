package org.valkyrienskies.loader.fabric

import com.googlecode.cqengine.query.Query
import com.googlecode.cqengine.query.QueryFactory
import com.googlecode.cqengine.resultset.ResultSet
import net.minecraft.util.math.BlockPos
import org.valkyrienskies.core.util.asLong
import org.valkyrienskies.util.cqengine.ConcurrentUpdatableIndexedCollection
import org.valkyrienskies.util.cqengine.UpdatableHashIndex
import org.valkyrienskies.util.cqengine.UpdatableUniqueIndex
import java.util.*
import java.util.function.BiConsumer
import java.util.stream.Stream

private fun <O> uniqueOrNull(res: ResultSet<O>): O? =
    if (res.isEmpty) null else res.uniqueResult()

/**
 * A class that keeps track of ship data
 */
class QueryableShipData // This tells Jackson to pass in allShips when serializing
// The default thing that is passed in will be 'null' if none exists
constructor(
    ships: ConcurrentUpdatableIndexedCollection<ShipData>? = null
) : Iterable<ShipData> {
    // Where every ship data instance is stored, regardless if the corresponding PhysicsObject is
    // loaded in the World or not.
    private val allShips: ConcurrentUpdatableIndexedCollection<ShipData> = ships ?: ConcurrentUpdatableIndexedCollection()

    @Deprecated("Do not use -- thinking of better API choices")
    fun getAllShips(): ConcurrentUpdatableIndexedCollection<ShipData> {
        return allShips
    }

    /**
     * Retrieves a list of all ships.
     */
    val ships: Collection<ShipData> = Collections.unmodifiableCollection(allShips)

    fun getShipFromChunk(chunkX: Int, chunkY: Int) = getShipFromChunk(asLong(chunkX, chunkY))

    fun getShipFromBlock(pos: BlockPos): ShipData? {
        return getShipFromChunk(pos.x shr 4, pos.z shr 4)
    }

    fun getShipFromChunk(chunkLong: Long): ShipData? {
        val query: Query<ShipData> = QueryFactory.equal(ShipData.CHUNKS, chunkLong)
        return allShips.retrieve(query).use(::uniqueOrNull)
    }

    fun getShip(uuid: UUID): ShipData? {
        val query: Query<ShipData> = QueryFactory.equal(ShipData.UUID, uuid)
        return allShips.retrieve(query).use(::uniqueOrNull)
    }

    fun getShipFromName(name: String): ShipData? {
        val query: Query<ShipData> = QueryFactory.equal(ShipData.NAME, name)
        return allShips.retrieve(query).use(::uniqueOrNull)
    }

    fun removeShip(uuid: UUID) {
        getShip(uuid).let { ship -> allShips.remove(ship) }
    }

    fun removeShip(data: ShipData?) {
        allShips.remove(data)
    }

    fun addShip(ship: ShipData) {
        allShips.add(ship)
    }

    fun registerUpdateListener(
        updateListener: BiConsumer<Iterable<ShipData>, Iterable<ShipData>>
    ) {
        allShips.registerUpdateListener(updateListener)
    }

    /**
     * Atomically updates ShipData. It must be true that `!oldData.equals(newData)`
     *
     * @param oldData The old data object(s) to replace
     * @param newData The new data object(s)
     */
    fun updateShipData(oldData: Iterable<ShipData>, newData: Iterable<ShipData>) {
        allShips.update(oldData, newData)
    }

    /**
     * Atomically updates ShipData. It must be true that `!oldData.equals(newData)`
     *
     * @param oldData The old data object to replace
     * @param newData The new data object
     */
    fun updateShipData(oldData: ShipData, newData: ShipData) {
        this.updateShipData(setOf(oldData), setOf(newData))
    }

    override fun iterator(): Iterator<ShipData> {
        return allShips.iterator()
    }

    fun stream(): Stream<ShipData> {
        return allShips.stream()
    }

    init {
        allShips.forEach { it.owner = this }

        allShips.addIndex(UpdatableHashIndex.onAttribute(ShipData.NAME))
        allShips.addIndex(UpdatableUniqueIndex.onAttribute(ShipData.UUID))
        allShips.addIndex(UpdatableUniqueIndex.onAttribute(ShipData.CHUNKS))
    }
}