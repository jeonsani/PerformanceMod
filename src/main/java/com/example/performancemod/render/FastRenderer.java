package com.example.performancemod.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import org.lwjgl.opengl.GL30;
import java.nio.ByteBuffer;

public class FastRenderer {
    private static final VertexFormat POSITION_COLOR_TEXTURE_LIGHT = VertexFormats.POSITION_COLOR_TEXTURE_LIGHT;
    private final VertexBuffer[] buffers;
    private final BufferBuilder bufferBuilder;
    private int currentBuffer = 0;
    
    public FastRenderer() {
        this.buffers = new VertexBuffer[3]; // Triple buffering
        for (int i = 0; i < buffers.length; i++) {
            this.buffers[i] = new VertexBuffer(VertexBuffer.Usage.DYNAMIC);
        }
        this.bufferBuilder = new BufferBuilder(8388608); // 8MB buffer
    }
    
    public void beginRender() {
        currentBuffer = (currentBuffer + 1) % buffers.length;
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, POSITION_COLOR_TEXTURE_LIGHT);
        
        // Enable hardware acceleration features
        GL30.glBindBufferBase(GL30.GL_UNIFORM_BUFFER, 0, 0);
        GL30.glEnable(GL30.GL_TEXTURE_2D_ARRAY);
    }
    
    public void endRender() {
        ByteBuffer data = bufferBuilder.end().getBuffer();
        buffers[currentBuffer].bind();
        buffers[currentBuffer].upload(data);
        
        // Cleanup
        GL30.glDisable(GL30.GL_TEXTURE_2D_ARRAY);
        GL30.glBindBufferBase(GL30.GL_UNIFORM_BUFFER, 0, 0);
    }
    
    public void dispose() {
        for (VertexBuffer buffer : buffers) {
            buffer.close();
        }
    }
} 