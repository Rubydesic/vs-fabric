package org.valkyrienskies.loader.fabric.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Shadow
    private Vec3d pos;
    @Shadow
    public World world;

    public Entity self = Entity.class.cast(this);

    @Inject(method = "move", at = @At("RETURN"), cancellable = true)
    private void postMove(MovementType type, Vec3d movement, CallbackInfo cb) {

    }

}
