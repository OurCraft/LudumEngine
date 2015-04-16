package org.lengine.render

import java.awt.image.BufferedImage
import java.io.InputStream
import java.nio.ByteBuffer
import java.util.{Map, HashMap}
import javax.imageio.ImageIO

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.{GL13, GL12}

object TextureRegistry {

  private val registry: Map[String, TextureData] = new HashMap[String, TextureData]

  def register(path: String, data: TextureData): Unit = {
    registry.put(path, data)
  }

  def hasData(path: String): Boolean = {
    registry.containsKey(path)
  }

  def getData(path: String): TextureData = {
    registry.get(path)
  }
}

class TextureRegion(val minU: Float = 0, val minV: Float = 0, val maxU: Float = 1, val maxV: Float = 1) {}

class TextureData(path: String) {
  private val input: InputStream = getClass.getResourceAsStream("/"+path)
  private val image: BufferedImage = ImageIO.read(input)
  val w: Int = image.getWidth
  val h: Int = image.getHeight
  private val pixels: Array[Int] = image.getRGB(0,0,w,h,null,0,w)
  private val buffer: ByteBuffer = ByteBuffer.allocateDirect(w*h*4)

  for(y <- 0 until h) {
    for(x <- 0 until w) {
      val color = pixels(x+y*w)
      val alpha: Byte = {
        color >> 24 & 0xFF
      }.toByte
      val red: Byte = {
        color >> 16 & 0xFF
      }.toByte
      val green: Byte = {
        color >> 8 & 0xFF
      }.toByte
      val blue: Byte = {
        color >> 0 & 0xFF
      }.toByte
      buffer.put(red)
      buffer.put(green)
      buffer.put(blue)
      buffer.put(alpha)
    }
  }
  buffer.flip
  val texID: Int = glGenTextures
  glBindTexture(GL_TEXTURE_2D, texID)
  glTexParameteri(GL_TEXTURE_2D, GL12.GL_TEXTURE_BASE_LEVEL, 0)
  glTexParameteri(GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 0)
  glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE)
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

  glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer)
}

class Texture(path: String) {
  var data: TextureData = _
  if(TextureRegistry.hasData(path)) {
    data = TextureRegistry.getData(path)
  } else {
    data = new TextureData(path)
    TextureRegistry.register(path, data)
  }
  val texID: Int = data.texID

  def bind(): Unit = bind(0)

  def bind(slot: Int): Unit = {
    GL13.glActiveTexture(GL13.GL_TEXTURE0 + slot)
    glBindTexture(GL_TEXTURE_2D, texID)
  }

  def getWidth = data.w

  def getHeight = data.h
}
