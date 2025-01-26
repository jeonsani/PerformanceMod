package com.example.performancemod.mixin;

import net.minecraft.server.world.ChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkManager.class)
public class ChunkMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void optimizeChunkTick(ServerWorld world, boolean tickChunks, CallbackInfo ci) {
        // Reduce chunk ticking frequency
        if (world.getTime() % 20 != 0) { // Only tick chunks every second
            ci.cancel();
        }
    }
} 