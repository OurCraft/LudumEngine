package org.jglrxavpok

/**
 * Created by jglrxavpok on 03/04/2015.
 */

import java.nio.{IntBuffer, ByteBuffer}

import org.lwjgl.opengl.GL11._
import org.lwjgl.system.glfw.GLFW._
import org.lwjgl.system.MemoryUtil._

class Window(var width: Int, var height: Int, var title: String = "OpenGL Window") {
  private var resizable = false
  private var visible = false
  private var windowPointer: Long = NULL

  glfwDefaultWindowHints()
  setVisible(false)
  setResizable(false)

  def getWidth = width

  def getHeight = height

  def getTitle = title

  def setTitle(newTitle: String) = title = newTitle

  def setWidth(newWidth: Int) = {
    width = newWidth
    glfwSetWindowSize(windowPointer, width, height)
  }

  def setHeight(newHeight: Int) = {
    height = newHeight
    glfwSetWindowSize(windowPointer, width, height)
  }

  def create = {
    windowPointer = glfwCreateWindow(width, height, title, NULL, NULL)

    glfwMakeContextCurrent(windowPointer)
    glfwSwapInterval(1)

    glfwShowWindow(windowPointer)
  }

  def setPos(x: Int, y: Int) = glfwSetWindowPos(windowPointer, x,y)

  def getPos(): (Int, Int) = {
    val widthBuffer: IntBuffer = ByteBuffer.allocateDirect(4).asIntBuffer()
    val heightBuffer: IntBuffer = ByteBuffer.allocateDirect(4).asIntBuffer()
    glfwGetWindowPos(windowPointer, widthBuffer, heightBuffer)
    return (widthBuffer.get(), heightBuffer.get())
  }

  def setResizable(_resizable: Boolean) = {
    glfwWindowHint(GLFW_RESIZABLE, if(_resizable) GL_TRUE else GL_FALSE)
    resizable = _resizable
  }

  def isResizable() = resizable

  def setVisible(_visible: Boolean) = {
    glfwWindowHint(GLFW_VISIBLE, if(_visible) GL_TRUE else GL_FALSE)
    visible = _visible
  }

  def isVisible() = visible

  def shouldClose() = glfwWindowShouldClose(windowPointer) == GL_TRUE

  def refresh: Unit = glfwSwapBuffers(windowPointer)

  def pollEvents: Unit = glfwPollEvents()

  def dispose: Unit = glfwDestroyWindow(windowPointer)
}
