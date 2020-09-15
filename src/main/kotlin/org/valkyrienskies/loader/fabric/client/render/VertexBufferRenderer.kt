package org.valkyrienskies.loader.fabric.client.render

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.gl.VertexBuffer
import net.minecraft.client.render.VertexFormatElement
import net.minecraft.client.render.VertexFormats
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

fun renderVertexBuffer(vertexBuffer: VertexBuffer) {
    // Check if optifine shaders are currently loaded.
    val areOptifineShadersEnabled = false
    GlStateManager.pushMatrix()
    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY)
//    OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit)
    GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY)
//    OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit)
    GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY)
//    OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit)
    GL11.glEnableClientState(GL11.GL_COLOR_ARRAY)

    // Extra OpenGL states that must be enabled when shaders are enabled.
    if (areOptifineShadersEnabled) {
        GL11.glEnableClientState(32885)
        GL20.glEnableVertexAttribArray(11)
        GL20.glEnableVertexAttribArray(12)
        GL20.glEnableVertexAttribArray(10)
    }
    GlStateManager.pushMatrix()
    vertexBuffer.bind()

    // Even more OpenGL states that must be enabled when shaders are enabled.
    if (areOptifineShadersEnabled) {
        val vertexSizeI = 14
        GL11.glVertexPointer(3, 5126, 56, 0L)
        GL11.glColorPointer(4, 5121, 56, 12L)
        GL11.glTexCoordPointer(2, 5126, 56, 16L)
//        OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit)
        GL11.glTexCoordPointer(2, 5122, 56, 24L)
//        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit)
        GL11.glNormalPointer(5120, 56, 28L)
        GL20.glVertexAttribPointer(11, 2, 5126, false, 56, 32L)
        GL20.glVertexAttribPointer(12, 4, 5122, false, 56, 40L)
        GL20.glVertexAttribPointer(10, 3, 5122, false, 56, 48L)
    } else {
        GL11.glVertexPointer(3, 5126, 28, 0)
        GL11.glColorPointer(4, 5121, 28, 12)
        GL11.glTexCoordPointer(2, 5126, 28, 16)
//        OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit)
        GL11.glTexCoordPointer(2, 5122, 28, 24)
//        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit)
    }

//    vertexBuffer.drawArrays(7)



    GlStateManager.popMatrix()
    VertexBuffer.unbind();
//    GlStateManager.resetColor()
    loop@ for (vfe in VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL.elements) {
        val enumUsage: VertexFormatElement.Type = vfe.type;
        val i: Int = vfe.index
        when (enumUsage) {
            VertexFormatElement.Type.POSITION -> {
                GL11.glDisableClientState(32884)
            }
            VertexFormatElement.Type.UV -> {
//                OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + i)
                GL11.glDisableClientState(32888)
//                OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit)
            }
            VertexFormatElement.Type.COLOR -> {
                GL11.glDisableClientState(32886)
//                GlStateManager.resetColor()
            }
            else -> continue@loop;
        }
    }
    VertexBuffer.unbind();

    // Finally disable some of those extra OpenGL states that were be enabled due to shaders.
    if (areOptifineShadersEnabled) {
        GL11.glDisableClientState(32885)
        GL20.glDisableVertexAttribArray(11)
        GL20.glDisableVertexAttribArray(12)
        GL20.glDisableVertexAttribArray(10)
    }
//    GlStateManager.resetColor()
    GlStateManager.popMatrix()
}