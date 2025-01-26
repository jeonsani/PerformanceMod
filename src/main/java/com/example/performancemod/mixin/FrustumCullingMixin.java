package com.example.performancemod.mixin;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class FrustumCullingMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void optimizeFrustumCulling(Entity entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        // Implement more aggressive frustum culling
        if (!frustum.isVisible(entity.getBoundingBox())) {
            cir.setReturnValue(false);
        }
    }
} 