package com.example.performancemod.mixin;

import com.example.performancemod.render.FastRenderer;
import net.minecraft.client.render.GameRenderer;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    private static final FastRenderer FAST_RENDERER = new FastRenderer();

    @Inject(method = "render", at = @At("HEAD"))
    private void optimizeRendering(CallbackInfo ci) {
        // Enable modern OpenGL features
        GL30.glBindVertexArray(GL30.glGenVertexArrays());
        
        // Enable vertex attribute arrays
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glEnableVertexAttribArray(2);
        
        // Use our fast renderer
        FAST_RENDERER.beginRender();
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void finishRendering(CallbackInfo ci) {
        FAST_RENDERER.endRender();
        
        // Cleanup
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glDisableVertexAttribArray(2);
    }
} 