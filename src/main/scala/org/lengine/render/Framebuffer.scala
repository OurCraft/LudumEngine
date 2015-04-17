package org.lengine.render

import java.nio.{IntBuffer, ByteBuffer}

import org.lwjgl.{LWJGLException, BufferUtils}
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._

class Framebuffer(val w: Int, val h: Int) {

  var colorBufferID: Int = _
  var id: Int = _

  def createColorBuffer = {
    colorBufferID = glGenTextures
    glBindTexture(GL_TEXTURE_2D, colorBufferID)
    glTexParameteri(GL_TEXTURE_2D, GL12.GL_TEXTURE_BASE_LEVEL, 0)
    glTexParameteri(GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 0)
    glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, null.asInstanceOf[ByteBuffer])
  }

  createColorBuffer
  createFBO

  def checkStatus = {
    val status: Int = glCheckFramebufferStatus(GL_FRAMEBUFFER)
    if (status != GL_FRAMEBUFFER_COMPLETE) {
      throw new LWJGLException("Framebuffer could not be created, status code: " + status)
    }
  }

  def createFBO = {
    id = glGenFramebuffers()
    bind
    glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorBufferID, 0) // link color buffer to fbo

    val drawData: IntBuffer = BufferUtils.createIntBuffer(1)
    drawData.put(GL_COLOR_ATTACHMENT0)
    drawData.flip
    glDrawBuffers(drawData)

    checkStatus
    unbind
  }

  def bind: Unit = {
    glBindFramebuffer(GL_FRAMEBUFFER, id)
  }

  def unbind: Unit = {
    glBindFramebuffer(GL_FRAMEBUFFER, 0)
  }

}
