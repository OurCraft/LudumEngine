package org.lengine.render

import org.lwjgl.opengl.{Display, DisplayMode}

class Window(var width: Int, var height: Int, var title: String = "OpenGL Window") {
  private var resizable = false

  setResizable(false)

  def getWidth = width

  def getHeight = height

  def getTitle = title

  def setTitle(newTitle: String) = title = newTitle

  def setWidth(newWidth: Int) = {
    width = newWidth
    updateSize
  }

  def updateSize: Unit = Display.setDisplayMode(new DisplayMode(width, height))

  def setHeight(newHeight: Int) = {
    height = newHeight
    updateSize
  }

  def create = {
    updateSize
    Display.create
  }

  def setPos(x: Int, y: Int) = Display.setLocation(x, y)

  def getPos: (Int, Int) = {
    (Display.getX, Display.getY)
  }

  def setResizable(_resizable: Boolean) = {
    Display.setResizable(_resizable)
    resizable = _resizable
  }

  def isResizable = resizable

  def isVisible = Display.isVisible

  def shouldClose = Display.isCloseRequested

  def refresh: Unit = {
    Display.sync(60)
    Display.update
  }

  def dispose: Unit = Display.destroy
}
