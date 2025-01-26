package com.example.performancemod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import java.util.HashSet;
import java.util.Set;

public class EntityOptimizer {
    private static final Set<EntityType<?>> LOW_PRIORITY_ENTITIES = new HashSet<>();
    private static final int MAX_VISIBLE_ENTITIES = 100;
    private static int visibleEntityCount = 0;
    
    static {
        // Add entities that can be culled more aggressively on low-end systems
        LOW_PRIORITY_ENTITIES.add(EntityType.ITEM);
        LOW_PRIORITY_ENTITIES.add(EntityType.EXPERIENCE_ORB);
        LOW_PRIORITY_ENTITIES.add(EntityType.FALLING_BLOCK);
    }
    
    public static boolean shouldRenderEntity(Entity entity) {
        if (visibleEntityCount > MAX_VISIBLE_ENTITIES) {
            if (LOW_PRIORITY_ENTITIES.contains(entity.getType())) {
                return false;
            }
        }
        
        // Only render entities within a smaller radius on low-end devices
        double renderDistanceSquared = entity.isPlayer() ? 1024.0 : 256.0; // 32 blocks for players, 16 for others
        if (entity.squaredDistanceTo(entity.getCameraPosVec(1.0F)) > renderDistanceSquared) {
            return false;
        }
        
        visibleEntityCount++;
        return true;
    }
    
    public static void resetEntityCount() {
        visibleEntityCount = 0;
    }
} 