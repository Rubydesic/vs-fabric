package org.valkyrienskies.loader.fabric

import net.minecraft.world.World
import org.valkyrienskies.core.util.attached

val World.shipData get() = this.attached.computeIfAbsent { QueryableShipData() }
val World.ships get() = this.attached.computeIfAbsent { ArrayList<PhysicsObject>() }