package org.valkyrienskies.loader.fabric.command

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.CommandManager
import net.minecraft.text.LiteralText
import org.joml.Matrix4d
import org.valkyrienskies.core.game.ChunkClaim
import org.valkyrienskies.loader.fabric.TransformableChunks
import org.valkyrienskies.loader.fabric.ships

object VSCommand {
    fun register() {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _ ->
            dispatcher.register(
                CommandManager.literal("vs")
                    .then(
                        CommandManager.literal("toShip").executes { ctx ->
                            val player = ctx.source.entity
                            require(player is PlayerEntity)

                            val chunkX = player.chunkX
                            val chunkZ = player.chunkZ

                            player.sendMessage(LiteralText("You are in $chunkX, $chunkZ"), false)

                            val cWorld = MinecraftClient.getInstance().world!!

                            val physo = TransformableChunks(cWorld, ChunkClaim(chunkX, chunkZ, 0),
                                Matrix4d().translate(80.0, 10.0, 0.0))
                            cWorld.ships.add(physo)

                            1
                        }
                    )
            )
        })
    }
}