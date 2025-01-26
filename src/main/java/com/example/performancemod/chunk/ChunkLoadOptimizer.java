package com.example.performancemod.chunk;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ChunkLoadOptimizer {
    private static final ThreadPoolExecutor CHUNK_LOADER = 
        (ThreadPoolExecutor) Executors.newFixedThreadPool(
            Math.max(1, Runtime.getRuntime().availableProcessors() - 1)
        );
    private static final ConcurrentHashMap<ChunkPos, Long> LAST_ACCESS = new ConcurrentHashMap<>();
    
    public static void optimizeChunkLoad(ServerWorld world, ChunkPos pos) {
        LAST_ACCESS.put(pos, System.currentTimeMillis());
        
        // Unload chunks that haven't been accessed in 30 seconds
        LAST_ACCESS.entrySet().removeIf(entry -> {
            if (System.currentTimeMillis() - entry.getValue() > 30000) {
                world.getChunkManager().unloadChunk(entry.getKey().x, entry.getKey().z);
                return true;
            }
            return false;
        });
    }
    
    public static void shutdown() {
        CHUNK_LOADER.shutdown();
    }
} 