package org.lengine

import java.io.File
import java.util.{Map, HashMap}

import org.lengine.maths.{Mat4f, Vec2f}
import org.lengine.render._
import org.lengine.sound.SoundManager
import org.lengine.utils.{SystemUtils, LWJGLSetup}
import org.lwjgl.input.{Controller, Controllers, Keyboard, Mouse}
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13

abstract class GameBase(id: String) extends App {

  var fps: Int = _
  var frames: Int = _
  var window: Window = null
  val keymap: Map[Int, Boolean] = new HashMap[Int, Boolean]
  var soundManager: SoundManager = _
  var framebuffer: Framebuffer = null
  var renderTarget: VertexArray = null
  var postProcessShader: Shader = null

  def getBaseHeight: Int

  def initOpenGL: Unit = {
    val ratio: Double = 16.0/9.0
    val height: Int = getBaseHeight
    val width: Int = (height*ratio).toInt

    val gameFolder = SystemUtils.getGameFolder(s"LudumEngine/$id")
    LWJGLSetup.load(new File(gameFolder.getParentFile, "natives"))
    window = new Window(width, height, id)
    window.create

    RenderEngine.setViewportSize(width, height)
    initFramebuffer(width, height)
  }


  def initFramebuffer(w: Int, h: Int): Unit = {
    framebuffer = new Framebuffer(w, h)
    renderTarget = new VertexArray
    renderTarget.defineVertex(new Vec2f(0,0), new Vec2f)
    renderTarget.defineVertex(new Vec2f(w,0), new Vec2f(1))
    renderTarget.defineVertex(new Vec2f(w,h), new Vec2f(1,1))
    renderTarget.defineVertex(new Vec2f(0,h), new Vec2f(0,1))

    renderTarget.defineIndex(1)
    renderTarget.defineIndex(0)
    renderTarget.defineIndex(2)

    renderTarget.defineIndex(2)
    renderTarget.defineIndex(0)
    renderTarget.defineIndex(3)

    renderTarget.compile
  }

  private def vomitFramebufferOnScreen = {
    val blitShader: Shader = if(postProcessShader == null) RenderEngine.baseShader else postProcessShader
    RenderEngine.setShader(blitShader)
    RenderEngine.setTransformMatrix(new Mat4f().identity)
    RenderEngine.setProjectionMatrix(new Mat4f().orthographic(0, RenderEngine.displayWidth, 0, RenderEngine.displayHeight))
    RenderEngine.clearColorBuffer(0,0,1,1)

    GL13.glActiveTexture(GL13.GL_TEXTURE0)
    glBindTexture(GL_TEXTURE_2D, framebuffer.colorBufferID)

    renderTarget.quickRender
    glBindTexture(GL_TEXTURE_2D, 0)
    blitShader.unbind

    RenderEngine.setShader(RenderEngine.baseShader)
  }

  def initOpenAL: Unit = {
    soundManager = new SoundManager
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

  def onAxisMoved(x: Float, y: Float, index: Int, source: Controller): Unit = {

  }

  def onButtonPressed(index: Int, source: Controller): Unit = {

  }

  def onButtonReleased(index: Int, source: Controller): Unit = {

  }

  def onPovYMoved(value: Float, index: Int, source: Controller): Unit = {

  }

  def onPovXMoved(value: Float, index: Int, source: Controller): Unit = {

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
      val keyState: Boolean = Keyboard.getEventKeyState
      val keyCode: Int = Keyboard.getEventKey
      val char: Char = Keyboard.getEventCharacter
      keymap.put(keyCode, keyState)
      if(keyState) {
        onKeyPressed(keyCode, char)
      } else {
        onKeyReleased(keyCode, char)
      }
    }

    while(Controllers.next) {
      val buttonState = Controllers.getEventButtonState
      val index = Controllers.getEventControlIndex
      val source = Controllers.getEventSource
      val xAxis = Controllers.getEventXAxisValue
      val yAxis = Controllers.getEventYAxisValue
      if(Controllers.isEventAxis) {
        onAxisMoved(xAxis, yAxis, index, source)
      } else if(Controllers.isEventButton) {
        if(buttonState) {
          onButtonPressed(index, source)
        } else {
          onButtonReleased(index, source)
        }
      } else if(Controllers.isEventPovX) {
        onPovXMoved(xAxis, index, source)
      } else if(Controllers.isEventPovX) {
        onPovYMoved(yAxis, index, source)
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

      RenderEngine.reset
      glEnable(GL_BLEND)
      glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
      framebuffer.bind
      RenderEngine.clearColorBuffer(0,0,0,1)
      RenderEngine.enableTextures

      val deltaTime: Float = ns / 1000000000.0f
      RenderEngine.update(deltaTime)
      render(deltaTime)
      framebuffer.unbind

      vomitFramebufferOnScreen
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

      soundManager.update()

      Thread.sleep(5)
    }
  }

  implicit def toTexture(texture: String): Texture = {
    new Texture(texture)
  }

  initOpenGL
  initOpenAL
  initGame
  loop
  soundManager.cleanup()
}
