package com.example.performancemod.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void optimizeEntityTick(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        
        // Skip entity ticking if too far from players
        if (!entity.isPlayer() && entity.world.isClient && entity.world.getClosestPlayer(entity, 32.0) == null) {
            ci.cancel();
        }
    }
} 