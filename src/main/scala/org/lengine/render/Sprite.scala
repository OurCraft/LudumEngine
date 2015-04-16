package org.lengine.render

import org.lengine.maths._

class Sprite(var texture: Texture, var region: TextureRegion = new TextureRegion) {


  var width: Float = texture.getWidth
  var height: Float = texture.getHeight

  private val transform: Transform = new Transform
  private val vertexArray = new VertexArray

  setCenter(width/2f, height/2f)

  vertexArray.defineVertex(new Vec2f, new Vec2f(region.minU, region.minV))
  vertexArray.defineVertex(new Vec2f(1), new Vec2f(region.maxU, region.minV))
  vertexArray.defineVertex(new Vec2f(1,1), new Vec2f(region.maxU, region.maxV))
  vertexArray.defineVertex(new Vec2f(0,1), new Vec2f(region.minU, region.maxV))

  vertexArray.defineIndex(1)
  vertexArray.defineIndex(0)
  vertexArray.defineIndex(2)

  vertexArray.defineIndex(2)
  vertexArray.defineIndex(0)
  vertexArray.defineIndex(3)
  vertexArray.compile()

  def render(delta: Float): Unit = {
    transform.scale.x = width
    transform.scale.y = height
    RenderEngine.setTransformMatrix(transform.toMatrix)
    texture.bind(0)
    vertexArray.bind()
    vertexArray.render()
  }

  def setPos(x: Float, y: Float): Unit = {
    transform.pos.x = x
    transform.pos.y = y
  }

  def setAngle(value: Float): Unit = {
    transform.angle = value
  }

  def getAngle(): Float = {
    transform.angle
  }

  def getCenter(): Vec2f = {
    transform.center
  }

  def setCenter(x: Float, y: Float): Unit = {
    transform.center.set(x, y)
  }

  def setCenter(c: Vec2f): Unit = {
    transform.center.set(c)
  }

  def getPos(): Vec2f = {
    transform.pos
  }
}
