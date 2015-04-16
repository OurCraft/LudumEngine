package org.lengine.maths

class Transform {
  val pos: Vec2f = new Vec2f
  val center: Vec2f = new Vec2f
  val scale: Vec2f = new Vec2f(1,1)
  var angle: Float = 0

  def rotationMatrix: Mat4f = {
    val forward: Vec3f = Z3
    val up: Vec3f = Y3.copy.rotate(angle)
    val right: Vec3f = up.copy.rotate(-(Math.PI/2f).toFloat)
    new Mat4f().rotation(0, 0, angle)
  }

  def toMatrix: Mat4f = {
    val translation: Mat4f = new Mat4f().translation(pos.x+center.x, pos.y+center.y, 0)
    val toCenterTranslation: Mat4f = new Mat4f().translation(-center.x,-center.y, 0)
    val rotation: Mat4f = rotationMatrix
    val scaling: Mat4f = new Mat4f().scale(scale.x, scale.y, 0)
    val result: Mat4f = translation.mul(rotation.mul(toCenterTranslation.mul(scaling)))
    result
  }
}
