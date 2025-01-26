package com.example.performancemod;

import com.example.performancemod.chunk.ChunkLoadOptimizer;
import com.example.performancemod.memory.MemoryManager;
import com.example.performancemod.system.LowEndOptimizer;
import com.example.performancemod.render.TextureOptimizer;
import com.example.performancemod.entity.EntityOptimizer;
import com.example.performancemod.render.ShaderOptimizer;
import com.example.performancemod.render.LightingOptimizer;
import com.example.performancemod.system.ThreadOptimizer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;

public class PerformanceMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register optimization handlers
        registerOptimizations();
        
        // Schedule periodic cleanup
        MemoryManager.scheduleCleanup(() -> {
            // Add cleanup tasks here
        });
        
        // Register shutdown hooks
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ChunkLoadOptimizer.shutdown();
            MemoryManager.shutdown();
        }));
        
        // Initialize low-end optimizations
        LowEndOptimizer.applyLowEndOptimizations();
        
        // Register cleanup tasks
        MemoryManager.scheduleCleanup(() -> {
            TextureOptimizer.clearCache();
            EntityOptimizer.resetEntityCount();
        });
        
        // Apply additional optimizations
        ShaderOptimizer.optimizeShaders();
        LightingOptimizer.reduceLightingQuality();
        ThreadOptimizer.optimizeThreadPriorities();
        
        // Register periodic optimization tasks
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            if (LightingOptimizer.shouldUpdateLighting()) {
                ShaderOptimizer.optimizeShaders();
            }
        });
    }

    private void registerOptimizations() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            // Optimize server performance
            optimizeServer(server);
        });
        
        ServerWorldEvents.LOAD.register((server, world) -> {
            // Optimize world loading
            world.getChunkManager().setViewDistance(8);
        });
    }

    private void optimizeServer(MinecraftServer server) {
        if (!server.getPlayerManager().getPlayerList().isEmpty()) {
            // Adjust settings based on player count
            int playerCount = server.getPlayerManager().getCurrentPlayerCount();
            server.getWorldProperties().setViewDistance(Math.max(6, 12 - playerCount));
        }
    }
} 