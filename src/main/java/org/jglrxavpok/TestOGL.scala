package org.jglrxavpok

import java.nio.ByteBuffer

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GLContext
import org.lwjgl.system.glfw.{GLFWvidmode, ErrorCallback}
import org.lwjgl.system.glfw.GLFW._

object TestOGL extends App {

  initOpenGL
  loop
  var window: Window = null

  var texture: Texture = null

  def initOpenGL: Unit = {
    val ratio: Double = 16.0/9.0
    val height: Int = 460
    val width: Int = (height*ratio).toInt
    val title = "OpenGL with Scala! :)"
    glfwSetErrorCallback(ErrorCallback.Util.getDefault)

    if(glfwInit != GL_TRUE)
      throw new IllegalStateException("Unable to initialize GLFW")
    window = new Window(width, height, title)
    window.create

    val vidmode: ByteBuffer = glfwGetVideoMode(glfwGetPrimaryMonitor())
    window.setPos((GLFWvidmode.width(vidmode) - width) / 2,(GLFWvidmode.height(vidmode) - height) / 2)
    print(window.getPos)

    GLContext.createFromCurrent()
    texture = new Texture("assets/textures/test.png")
  }

  def loop: Unit = {
    while(!window.shouldClose) {
      glClearColor(0,0,0,1)
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
      glColor3f(1,0,0)
      glRectf(0,0,1,1)

      glColor3f(1,1,1)
      texture.bind
      glEnable(GL_TEXTURE_2D)
      glBegin(GL_QUADS)

        glTexCoord2f(0,0)
        glVertex2f(-1f,-1f)

        glTexCoord2f(1,0)
        glVertex2f(1f,-1f)

        glTexCoord2f(1,1)
        glVertex2f(1f,1f)

        glTexCoord2f(0,1)
        glVertex2f(-1f,1f)
      glEnd
      window.refresh
      window.pollEvents
    }

    window.dispose
  }
}