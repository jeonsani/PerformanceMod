package com.example.performancemod.render;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.AbstractTexture;
import java.util.concurrent.ConcurrentHashMap;

public class TextureOptimizer {
    private static final ConcurrentHashMap<String, NativeImage> TEXTURE_CACHE = new ConcurrentHashMap<>();
    private static final int MAX_TEXTURE_SIZE = 512; // Limit texture size for low-end devices
    
    public static void optimizeTexture(AbstractTexture texture, NativeImage image) {
        if (image.getWidth() > MAX_TEXTURE_SIZE || image.getHeight() > MAX_TEXTURE_SIZE) {
            // Downscale large textures
            NativeImage scaled = new NativeImage(
                Math.min(image.getWidth(), MAX_TEXTURE_SIZE),
                Math.min(image.getHeight(), MAX_TEXTURE_SIZE),
                false
            );
            image.resizeSubRectTo(
                0, 0, image.getWidth(), image.getHeight(),
                scaled
            );
            image.close();
            TEXTURE_CACHE.put(texture.toString(), scaled);
        }
    }
    
    public static void clearCache() {
        TEXTURE_CACHE.values().forEach(NativeImage::close);
        TEXTURE_CACHE.clear();
    }
} 