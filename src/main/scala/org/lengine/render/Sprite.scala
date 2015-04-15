package org.lengine.render

import org.lengine.maths.{Transform, Vec2f}

class Sprite(var texture: Texture) {

  var x = 0
  var y = 0
  var width: Float = texture.getWidth
  var height: Float = texture.getHeight

  private val transform: Transform = new Transform
  private val vertexArray = new VertexArray

  vertexArray.defineVertex(new Vec2f, new Vec2f)
  vertexArray.defineVertex(new Vec2f(1), new Vec2f(1))
  vertexArray.defineVertex(new Vec2f(1,1), new Vec2f(1,1))
  vertexArray.defineVertex(new Vec2f(0,1), new Vec2f(0,1))

  vertexArray.defineIndex(1)
  vertexArray.defineIndex(0)
  vertexArray.defineIndex(2)

  vertexArray.defineIndex(2)
  vertexArray.defineIndex(0)
  vertexArray.defineIndex(3)
  vertexArray.compile()

  def render(): Unit = {
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
}
