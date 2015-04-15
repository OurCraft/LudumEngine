package org.lengine.render

import java.nio.FloatBuffer
import java.util.{HashMap, Map}

import org.lengine.maths.{Vec2f, Mat4f}
import org.lengine.utils.IOUtils
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20._

class Shader(shaderBasePath: String) {

  val uniforms: Map[String, Int] = new HashMap[String, Int]

  val vertexPath: String = shaderBasePath+".vsh"
  val fragPath: String = shaderBasePath+".fsh"
  val vertexContent: String = IOUtils.read(vertexPath, "UTF-8")
  val fragContent: String = IOUtils.read(fragPath, "UTF-8")

  val programID: Int = glCreateProgram
  val vertexID: Int = glCreateShader(GL_VERTEX_SHADER)
  val fragID: Int = glCreateShader(GL_FRAGMENT_SHADER)

  glShaderSource(vertexID, vertexContent)
  glShaderSource(fragID, fragContent)

  glCompileShader(vertexID)
  glCompileShader(fragID)

  if (glGetShaderi(vertexID, GL_COMPILE_STATUS) == 0) {
    val error: String = glGetShaderInfoLog(vertexID, 1024)
    println(s"Error while compiling vertex shader: $error")
  }

  if (glGetShaderi(fragID, GL_COMPILE_STATUS) == 0) {
    val error: String = glGetShaderInfoLog(fragID, 1024)
    println(s"Error while compiling fragment shader: $error")
  }

  glAttachShader(programID, vertexID)
  glAttachShader(programID, fragID)

  glLinkProgram(programID)

  if (glGetProgrami(programID, GL_LINK_STATUS) == 0) {
    val error: String = glGetProgramInfoLog(programID, 1024)
    println(s"Error while linking shader: $error")
  }
  glValidateProgram(programID)
  if (glGetProgrami(programID, GL_VALIDATE_STATUS) == 0) {
    val error: String = glGetProgramInfoLog(programID, 1024)
    println(s"Error while validating shader: $error")
  }

  def getUniformLoc(name: String): Int = {
    if(uniforms.containsKey(name)) {
      uniforms.get(name)
    } else {
      val id: Int = glGetUniformLocation(programID, name)
      uniforms.put(name, id)
      id
    }
  }

  def toBuffer(mat: Mat4f): FloatBuffer = {
    val buffer: FloatBuffer = BufferUtils.createFloatBuffer(16)
    for(y <- 0 until 4) {
      for(x <- 0 until 4) {
        buffer.put(mat.get(x,y))
      }
    }
    buffer.flip()
    buffer
  }

  def setUniform2f(s: String, v: Vec2f): Unit = {
    setUniform2f(s, v.x, v.y)
  }

  def setUniform2f(s: String, x: Float, y: Float): Unit = {
    val loc: Int = getUniformLoc(s)
    if(loc != -1) {
      glUniform2f(loc, x, y)
    } else {
      println(s"Could not update uniform $s")
    }
  }

  def setUniformf(s: String, value: Float): Unit = {
    val loc: Int = getUniformLoc(s)
    if(loc != -1) {
      glUniform1f(loc, value)
    } else {
      println(s"Could not update uniform $s")
    }
  }

  def setUniformi(s: String, value: Int): Unit = {
    val loc: Int = getUniformLoc(s)
    if(loc != -1) {
      glUniform1i(loc, value)
    } else {
      println(s"Could not update uniform $s")
    }
  }

  def setUniformMat4(s: String, mat: Mat4f): Unit = {
    val loc: Int = getUniformLoc(s)
    if(loc != -1) {
      glUniformMatrix4(loc, false, toBuffer(mat))
    } else {
      println(s"Could not update uniform $s")
    }
  }

  def bind(): Unit = {
    glUseProgram(programID)
  }

  def unbind(): Unit = {
    glUseProgram(0)
  }
}
