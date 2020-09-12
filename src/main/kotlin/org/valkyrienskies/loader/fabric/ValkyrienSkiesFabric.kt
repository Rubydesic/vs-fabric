package org.valkyrienskies.loader.fabric

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.text.LiteralText
import org.joml.Matrix4d
import org.valkyrienskies.core.game.ChunkClaim
import org.valkyrienskies.loader.fabric.command.VSCommand

@Suppress("unused")
fun init() {
    // This code runs as soon as Minecraft is in a mod-load-ready state.
    // However, some things (like resources) may still be uninitialized.
    // Proceed with mild caution.

    println("Hello Fabric world!")

    VSCommand.register()
}