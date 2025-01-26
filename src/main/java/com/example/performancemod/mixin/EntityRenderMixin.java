package com.example.performancemod.mixin;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void optimizeEntityRendering(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        // Skip rendering entities that are too far away
        if (entity.squaredDistanceTo(entity.getCameraPosVec(1.0F)) > 1024) { // 32 blocks
            cir.setReturnValue(false);
        }
    }
} 