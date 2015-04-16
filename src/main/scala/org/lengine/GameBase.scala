package org.lengine

import java.io.File
import java.util.{Map, HashMap}

import org.lengine.render.{TextureAtlas, RenderEngine, Window, Texture}
import org.lengine.utils.{SystemUtils, LWJGLSetup}
import org.lwjgl.input.{Keyboard, Mouse}
import org.lwjgl.opengl.GL11._

abstract class GameBase(id: String) extends App {

  var fps: Int = _
  var frames: Int = _
  var window: Window = null
  val keymap: Map[Int, Boolean] = new HashMap[Int, Boolean]

  def getBaseHeight: Int

  def initOpenGL: Unit = {
    val ratio: Double = 16.0/9.0
    val height: Int = getBaseHeight
    val width: Int = (height*ratio).toInt
    val title = "OpenGL with Scala! :)"

    val gameFolder = SystemUtils.getGameFolder(s"LudumEngine/$id")
    LWJGLSetup.load(new File(gameFolder.getParentFile, "natives"))
    window = new Window(width, height, title)
    window.create

    RenderEngine.setViewportSize(width, height)
  }

  def initGame: Unit

  def render(delta: Float): Unit

  def update(delta: Float): Unit

  def checkOpenGLError(trailing: String) = {
    val error: Int = glGetError
    if(error != GL_NO_ERROR) {
      println(s"OpenGL Error: $error")
    }
  }

  def onScroll(x: Int, y: Int, dir: Int): Unit

  def onMouseMoved(x: Int, y: Int, dx: Int, dy: Int): Unit

  def onMousePressed(x: Int, y: Int, button: Int): Unit

  def onMouseReleased(x: Int, y: Int, button: Int): Unit

  def onKeyPressed(keyCode: Int, char: Char): Unit

  def onKeyReleased(keyCode: Int, char: Char): Unit

  def isKeyPressed(keycode: Int): Boolean = {
    if(keymap.containsKey(keycode))
      keymap.get(keycode)
    else
      false
  }

  def pollEvents(d: Float) = {
    while(Mouse.next) {
      val dwheel: Int = Math.signum(Mouse.getEventDWheel).toInt
      val mouseButton: Int = Mouse.getEventButton
      val state: Boolean = Mouse.getEventButtonState
      val x: Int = Mouse.getEventX
      val y: Int = Mouse.getEventY
      val dx: Int = Mouse.getEventDX
      val dy: Int = Mouse.getEventDY

      if(dwheel != 0) {
        onScroll(x, y, dwheel)
      } else if(mouseButton != -1) {
        if(state) {
          onMousePressed(x, y, mouseButton)
        } else {
          onMouseReleased(x, y, mouseButton)
        }
      } else {
        onMouseMoved(x, y, dx, dy)
      }
    }

    while(Keyboard.next) {
      val keystate: Boolean = Keyboard.getEventKeyState
      val keyCode: Int = Keyboard.getEventKey
      val char: Char = Keyboard.getEventCharacter
      keymap.put(keyCode, keystate)
      if(keystate) {
        onKeyPressed(keyCode, char)
      } else {
        onKeyReleased(keyCode, char)
      }
    }
  }

  def loop: Unit = {
    var lastTime: Long = System.nanoTime
    var delta: Float = 0.0f
    val ns: Float = 1000000000.0f / 60.0f
    var timer: Long = System.currentTimeMillis
    var updates: Int = 0
    var running: Boolean = true
    while (running) {
      val now: Long = System.nanoTime
      var polledInput: Boolean = false
      delta += (now - lastTime) / ns
      lastTime = now
      while (delta >= 1.0) {
        val deltaTime: Float = ns / 1000000000.0f // TODO: use a proper system
        if (!polledInput) {
          pollEvents(deltaTime)
          polledInput = true
        }
        update(deltaTime)
        updates += 1
        delta -= 1
      }

      RenderEngine.clearColorBuffer(0,0,0,1)
      RenderEngine.enableTextures
      render(ns / 1000000000.0f)
      window.refresh

      frames += 1
      if (System.currentTimeMillis - timer > 1000) {
        timer += 1000
        window.setTitle(s"'$id' - $fps fps")
        fps = frames
        updates = 0
        frames = 0
      }
      if (window.shouldClose) running = false
    }
  }

  implicit def toTexture(texture: String): Texture = {
    new Texture(texture)
  }

  initOpenGL
  initGame
  loop
}