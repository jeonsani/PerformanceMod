package com.example.performancemod.memory;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MemoryManager {
    private static final ConcurrentLinkedQueue<Runnable> CLEANUP_TASKS = new ConcurrentLinkedQueue<>();
    private static final long MAX_MEMORY = Runtime.getRuntime().maxMemory();
    private static final ScheduledExecutorService MEMORY_CHECKER = Executors.newSingleThreadScheduledExecutor();
    private static final float MEMORY_THRESHOLD = 0.75f; // 75% memory usage threshold
    
    static {
        MEMORY_CHECKER.scheduleAtFixedRate(() -> {
            checkMemory();
        }, 30, 30, TimeUnit.SECONDS);
    }
    
    public static void scheduleCleanup(Runnable task) {
        CLEANUP_TASKS.offer(task);
    }
    
    private static void checkMemory() {
        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        if (usedMemory > MAX_MEMORY * MEMORY_THRESHOLD) {
            runCleanup();
        }
    }
    
    public static void runCleanup() {
        while (!CLEANUP_TASKS.isEmpty()) {
            try {
                CLEANUP_TASKS.poll().run();
            } catch (Exception e) {
                // Log error but continue cleanup
                System.err.println("Error during cleanup: " + e.getMessage());
            }
        }
        System.gc();
    }
    
    public static void shutdown() {
        MEMORY_CHECKER.shutdown();
    }
} 