package org.lengine.render

import org.lengine.maths._

class Sprite(var texture: Texture, var region: TextureRegion = new TextureRegion, var baseColor: Quaternion = new Quaternion(1,1,1,1)) {


  var width: Float = texture.getWidth * (region.maxU-region.minU)
  var height: Float = texture.getHeight * (region.maxV-region.minV)

  private val transform: Transform = new Transform
  private val vertexArray = new VertexArray

  setCenter(width/2f, height/2f)

  vertexArray.defineVertex(new Vec3f, new Vec2f(region.minU, region.maxV), baseColor)
  vertexArray.defineVertex(new Vec3f(1), new Vec2f(region.maxU, region.maxV), baseColor)
  vertexArray.defineVertex(new Vec3f(1,1), new Vec2f(region.maxU, region.minV), baseColor)
  vertexArray.defineVertex(new Vec3f(0,1), new Vec2f(region.minU, region.minV), baseColor)

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
    val oldMatrix: Mat4f = RenderEngine.transformMatrix
    RenderEngine.setTransformMatrix(transform.toMatrix)
    texture.bind(0)
    vertexArray.quickRender

    RenderEngine.setTransformMatrix(oldMatrix)
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
