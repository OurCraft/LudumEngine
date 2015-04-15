package org.lengine.render

import java.nio.{IntBuffer, FloatBuffer, ByteBuffer}
import java.util.{ArrayList, List}

import org.lengine.maths.{Vec3f, Vec2f}
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._

class VertexArray {

  private val positions: List[Vec3f] = new ArrayList[Vec3f]
  private val texCoords: List[Vec2f] = new ArrayList[Vec2f]
  private val indices: List[Int] = new ArrayList[Int]
  private var posID: Int = _
  private var coordsID: Int = _
  private var indicesID: Int = _
  private var vao: Int = _
  private var size: Int = _

  def defineVertex(pos: Vec2f, texCoord: Vec2f): Unit = {
    defineVertex(pos.toVec3(), texCoord)
  }

  def defineVertex(pos: Vec3f, texCoord: Vec2f): Unit = {
    positions add pos
    texCoords add texCoord
  }

  def defineIndex(indice: Int): Unit = {
    indices add indice
  }


  def compile(): Unit = {
    val posBuffer: FloatBuffer = toFloatBuffer3(positions)
    val coordBuffer: FloatBuffer = toFloatBuffer2(texCoords)
    val indiceBuffer: IntBuffer = toIntBuffer(indices)
    size = indices.size
    posID = glGenBuffers
    coordsID = glGenBuffers
    indicesID = glGenBuffers

    vao = glGenVertexArrays
    glBindVertexArray(vao)

    glBindBuffer(GL_ARRAY_BUFFER, posID)
    glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW)
    glVertexAttribPointer(ShaderConstants.POSITION_INDEX, 3, GL_FLOAT, false, 0, 0)
    glEnableVertexAttribArray(ShaderConstants.POSITION_INDEX)

    glBindBuffer(GL_ARRAY_BUFFER, coordsID)
    glBufferData(GL_ARRAY_BUFFER, coordBuffer, GL_STATIC_DRAW)
    glVertexAttribPointer(ShaderConstants.TEXT_INDEX, 2, GL_FLOAT, false, 0, 0)
    glEnableVertexAttribArray(ShaderConstants.TEXT_INDEX)

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesID)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indiceBuffer, GL_STATIC_DRAW)

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
    glBindBuffer(GL_ARRAY_BUFFER, 0)
    glBindVertexArray(0)
  }

  def bind(): Unit = {
    glBindVertexArray(vao)
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesID)
  }

  def unbind(): Unit = {
    glBindVertexArray(0)
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
  }

  def render(): Unit = {
    glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0)
  }

  def toFloatBuffer3(list: List[Vec3f]): FloatBuffer = {
    val n: Int = list.size
    val buffer: FloatBuffer = BufferUtils.createFloatBuffer(n*3)
    for(i <- 0 until n) {
      val vec: Vec3f = list.get(i)
      buffer.put(vec.x)
      buffer.put(vec.y)
      buffer.put(vec.z)
    }
    buffer.flip()
    buffer
  }

  def toFloatBuffer2(list: List[Vec2f]): FloatBuffer = {
    val n: Int = list.size
    val buffer: FloatBuffer = BufferUtils.createFloatBuffer(n*2)
    for(i <- 0 until n) {
      val vec: Vec2f = list.get(i)
      buffer.put(vec.x)
      buffer.put(vec.y)
    }
    buffer.flip()
    buffer
  }

  def toIntBuffer(list: List[Int]): IntBuffer = {
    val n: Int = list.size
    val buffer: IntBuffer = BufferUtils.createIntBuffer(n)
    for(i <- 0 until n) {
      buffer.put(list.get(i))
    }
    buffer.flip()
    buffer
  }

}
