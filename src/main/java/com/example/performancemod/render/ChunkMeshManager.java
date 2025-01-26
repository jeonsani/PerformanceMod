package com.example.performancemod.render;

import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.BlockPos;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkMeshManager {
    private static final ConcurrentHashMap<BlockPos, ChunkBuilder.BuiltChunk> CHUNK_CACHE = new ConcurrentHashMap<>();
    private static final int MAX_CACHE_SIZE = 512;

    public static void cacheChunk(BlockPos pos, ChunkBuilder.BuiltChunk chunk) {
        if (CHUNK_CACHE.size() >= MAX_CACHE_SIZE) {
            CHUNK_CACHE.clear();
        }
        CHUNK_CACHE.put(pos, chunk);
    }

    public static ChunkBuilder.BuiltChunk getCachedChunk(BlockPos pos) {
        return CHUNK_CACHE.get(pos);
    }
} 