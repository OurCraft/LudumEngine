package org.lengine

import java.io.File

import org.lengine.render.{Window, Texture}
import org.lengine.utils.{SystemUtils, LWJGLSetup}
import org.lwjgl.opengl.GL11._

abstract class GameBase(id: String) extends App {

  initOpenGL
  initGame
  loop
  var window: Window = null

  var texture: Texture = null

  def initOpenGL: Unit = {
    val ratio: Double = 16.0/9.0
    val height: Int = 460
    val width: Int = (height*ratio).toInt
    val title = "OpenGL with Scala! :)"

    val gameFolder = SystemUtils.getGameFolder(s"LudumEngine/$id")
    LWJGLSetup.load(new File(gameFolder.getParentFile, "natives"))
    window = new Window(width, height, title)
    window.create

    print(window.getPos)

    texture = new Texture("assets/textures/test.png")
  }

  def initGame: Unit

  def render: Unit

  def update: Unit

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

      render
      window.refresh
    }

    window.dispose
  }
}