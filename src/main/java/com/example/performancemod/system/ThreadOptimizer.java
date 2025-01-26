package com.example.performancemod.system;

public class ThreadOptimizer {
    public static void optimizeThreadPriorities() {
        // Set main render thread to highest priority
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        
        // Lower priority of background tasks
        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);
        
        for (Thread thread : threads) {
            if (thread != null && !isRenderThread(thread)) {
                thread.setPriority(Thread.MIN_PRIORITY);
            }
        }
    }
    
    private static boolean isRenderThread(Thread thread) {
        return thread.getName().contains("Render") || 
               thread.getName().contains("Client") ||
               thread.getName().contains("Main");
    }
} 