package org.lengine.maths

class Transform {
  val pos: Vec2f = new Vec2f
  val center: Vec2f = new Vec2f
  val scale: Vec2f = new Vec2f(1,1)
  val rotationObject = new Quaternion()
  var angle: Float = 0

  def toMatrix: Mat4f = {
    val translation: Mat4f = new Mat4f().translation(pos.x+center.x, pos.y+center.y, 0)
    val toCenterTranslation: Mat4f = new Mat4f().translation(-center.x, -center.y, 0)
    val rotation: Mat4f = rotationObject.toRotationMatrix
    val scaling: Mat4f = new Mat4f().scale(scale.x, scale.y, 1)
    val result: Mat4f = translation.mul(rotation.mul(toCenterTranslation.mul(scaling)))
    result
  }
}
