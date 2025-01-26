package com.example.performancemod.mixin;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    private static final int MAX_PARTICLES = 100; // Reduced maximum particles for low-end devices
    
    @Inject(method = "addParticle", at = @At("HEAD"), cancellable = true)
    private void limitParticles(Particle particle, CallbackInfo ci) {
        ParticleManager manager = (ParticleManager) (Object) this;
        if (manager.particles.size() >= MAX_PARTICLES) {
            ci.cancel();
        }
    }
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void optimizeParticleTick(CallbackInfo ci) {
        // Update particles less frequently
        if (Math.random() > 0.25) { // Only update 25% of particles each tick
            ci.cancel();
        }
    }
} 