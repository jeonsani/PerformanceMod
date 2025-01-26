package com.example.performancemod.mixin;

import net.minecraft.client.render.chunk.ChunkRenderer;
import net.minecraft.client.render.chunk.ChunkRenderData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkRenderer.class)
public class ChunkRendererMixin {
    private static final int MAX_REBUILD_CHUNKS_PER_FRAME = 2;
    private static int rebuildsThisFrame = 0;
    
    @Inject(method = "rebuild", at = @At("HEAD"), cancellable = true)
    private void limitChunkRebuilds(CallbackInfo ci) {
        if (rebuildsThisFrame >= MAX_REBUILD_CHUNKS_PER_FRAME) {
            ci.cancel();
            return;
        }
        rebuildsThisFrame++;
    }
    
    @Inject(method = "render", at = @At("HEAD"))
    private void resetRebuildCounter(CallbackInfo ci) {
        rebuildsThisFrame = 0;
    }
} 