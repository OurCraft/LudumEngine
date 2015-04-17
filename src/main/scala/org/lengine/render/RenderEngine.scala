package org.lengine.render

import org.lengine.maths.Mat4f
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL11._

object RenderEngine {


  private val init: Mat4f = new Mat4f().identity
  var displayWidth: Int = 0
  var displayHeight: Int = 0
  var shader: Shader = _
  var transformMatrix: Mat4f = _
  var projectionMatrix: Mat4f = _
  val baseShader: Shader = new Shader("assets/shaders/base")
  var time: Float = _

  reset

  def reset: Unit = {
    setShader(baseShader)
    setTransformMatrix(new Mat4f().identity)
    setViewportSize(displayWidth, displayHeight)
  }

  def update(delta: Float) = {
      time+=delta
  }

  def setShader(newShader: Shader): Unit = {
    shader = newShader

    if(shader != null) {
      shader.bind
      if(shader.getUniformLoc("time") >= 0) {
        shader.setUniformf("time", time)
      }

      if(shader.getUniformLoc("screenSize") != -1) {
        shader.setUniform2f("screenSize", displayWidth, displayHeight)
      }
    }
    else {
      GL20.glUseProgram(0)
    }
  }

  def setViewportSize(width: Int, height: Int): Unit = {
    displayWidth = width
    displayHeight = height
    setProjectionMatrix(new Mat4f().orthographic(0, width, 0, height))
  }

  def setProjectionMatrix(mat: Mat4f): Unit = {
    projectionMatrix = mat
    if(shader != null) {
      shader.setUniformMat4("projection", mat)
    }
  }

  def setTransformMatrix(mat: Mat4f): Unit = {
    transformMatrix = mat
    if(shader != null) {
      shader.setUniformMat4("transform", mat)
    }
  }

  def initTransform = {
    setTransformMatrix(init)
  }

  def enableTextures = {
    glEnable(GL_TEXTURE_2D)
  }

  def clearColorBuffer(r: Float, g: Float, b: Float, a: Float): Unit = {
    glColor4f(r,g,b,a)
    glClear(GL_COLOR_BUFFER_BIT)
  }
}
