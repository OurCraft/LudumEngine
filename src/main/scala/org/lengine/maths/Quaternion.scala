package org.lengine.maths

object Quaternions {
  def create(axis: Vec3f, angle: Float): Quaternion = {
    val sinHalfAngle: Float = Math.sin(angle / 2).toFloat
    val cosHalfAngle: Float = Math.cos(angle / 2).toFloat

    val x = axis.x * sinHalfAngle
    val y = axis.y * sinHalfAngle
    val z = axis.z * sinHalfAngle
    val w = cosHalfAngle
    new Quaternion(x,y,z,w)
  }

}

class Quaternion(var x: Float = 0, var y: Float = 0, var z: Float = 0, var w: Float = 1) {

  def toRotationMatrix(): Mat4f = {
    val res: Mat4f = new Mat4f
    res.rotation(forward, up, right)
  }

  def forward: Vec3f = {
    return new Vec3f(0, 0, 1).rotate(this)
  }

  def back: Vec3f = {
    return new Vec3f(0, 0, -1).rotate(this)
  }

  def up: Vec3f = {
    return new Vec3f(0, 1, 0).rotate(this)
  }

  def down: Vec3f = {
    return new Vec3f(0, -1, 0).rotate(this)
  }

  def right: Vec3f = {
    return new Vec3f(1, 0, 0).rotate(this)
  }

  def left: Vec3f = {
    return new Vec3f(-1, 0, 0).rotate(this)
  }

  def length: Float = {
    return Math.sqrt(x * x + y * y + z * z + w * w).asInstanceOf[Float]
  }

  def nlerp(dest: Quaternion, lerpFactor: Float, shortest: Boolean): Quaternion = {
    var correctedDest: Quaternion = dest
    if (shortest && this.dot(dest) < 0) correctedDest = new Quaternion(-dest.x, -dest.y, -dest.z, -dest.w)
    return correctedDest.sub(this).mul(lerpFactor).add(this).normalize
  }

  def slerp(dest: Quaternion, lerpFactor: Float, shortest: Boolean): Quaternion = {
    val EPSILON: Float = 1e3f
    var cos: Float = this.dot(dest)
    var correctedDest: Quaternion = dest
    if (shortest && cos < 0) {
      cos = -cos
      correctedDest = new Quaternion(-dest.x, -dest.y, -dest.z, -dest.w)
    }
    if (Math.abs(cos) >= 1 - EPSILON) return nlerp(correctedDest, lerpFactor, false)
    val sin: Float = Math.sqrt(1.0f - cos * cos).asInstanceOf[Float]
    val angle: Float = Math.atan2(sin, cos).asInstanceOf[Float]
    val invSin: Float = 1.0f / sin
    val srcFactor: Float = (Math.sin((1.0f - lerpFactor) * angle) * invSin).asInstanceOf[Float]
    val destFactor: Float = (Math.sin((lerpFactor) * angle) * invSin).asInstanceOf[Float]
    return this.mul(srcFactor).add(correctedDest.mul(destFactor)).normalize
  }

  def sub(r: Quaternion): Quaternion = {
    return new Quaternion(x - r.x, y - r.y, z - r.z, w - r.w)
  }

  def add(r: Quaternion): Quaternion = {
    return new Quaternion(x + r.x, y + r.y, z + r.z, w + r.w)
  }

  def dot(r: Quaternion): Float = {
    return x * r.x + y * r.y + z * r.z + w * r.w
  }

  def normalize: Quaternion = {
    val l: Float = length
    x /= l
    y /= l
    z /= l
    w /= l
    return this
  }

  def conjugate: Quaternion = {
    return new Quaternion(-x, -y, -z, w)
  }

  def mul(d: Float): Quaternion = {
    return new Quaternion(x * d, y * d, z * d, w * d)
  }

  def mul(r: Quaternion): Quaternion = {
    val w_ : Float = w * r.w - x * r.x - y * r.y - z * r.z
    val x_ : Float = x * r.w + w * r.x + y * r.z - z * r.y
    val y_ : Float = y * r.w + w * r.y + z * r.x - x * r.z
    val z_ : Float = z * r.w + w * r.z + x * r.y - y * r.x
    return new Quaternion(x_, y_, z_, w_)
  }

  def mul(r: Vec3f): Quaternion = {
    val w_ : Float = -x * r.x - y * r.y - z * r.z
    val x_ : Float = w * r.x + y * r.z - z * r.y
    val y_ : Float = w * r.y + z * r.x - x * r.z
    val z_ : Float = w * r.z + x * r.y - y * r.x
    return new Quaternion(x_, y_, z_, w_)
  }

}
