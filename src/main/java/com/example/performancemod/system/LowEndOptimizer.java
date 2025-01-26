package com.example.performancemod.system;

import org.lwjgl.opengl.GL11;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class LowEndOptimizer {
    private static final OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    private static final boolean IS_LOW_END = detectLowEndSystem();
    
    private static boolean detectLowEndSystem() {
        long totalMemory = osBean.getTotalMemorySize() / (1024 * 1024); // Convert to MB
        int processors = Runtime.getRuntime().availableProcessors();
        
        return totalMemory < 8192 || processors <= 4; // Less than 8GB RAM or 4 cores
    }
    
    public static void applyLowEndOptimizations() {
        if (IS_LOW_END) {
            // Reduce texture quality
            GL11.glHint(GL11.GL_TEXTURE_COMPRESSION_HINT, GL11.GL_FASTEST);
            
            // Disable fancy graphics features
            System.setProperty("java.awt.headless", "true");
            System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
        }
    }
} 